package com.bsd.specialproject.ui.searchresult

import android.location.Location
import androidx.lifecycle.*
import com.bsd.specialproject.constants.*
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.ui.searchresult.model.*
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.bsd.specialproject.utils.toDefaultValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class SearchResultViewModel(private val appPreference: AppPreference) : ViewModel() {
    private val creditCardCollectionStore by lazy {
        Firebase.firestore.collection(CREDIT_CARD_LIST)
    }
    private val promotionCollectionStore by lazy {
        Firebase.firestore.collection(PROMOTION_LIST)
    }

    private val myCreditCardIds by lazy {
        appPreference.myCreditCards?.toList()
    }

    private val _creditCardSearchResultList = MutableLiveData<List<CreditCardSearchResultModel>>()
    val creditCardSearchResultList: LiveData<List<CreditCardSearchResultModel>> =
        _creditCardSearchResultList

    private val _foryouPromotionList = MutableLiveData<List<ForYouPromotionModel>>()
    val foryouPromotionList: LiveData<List<ForYouPromotionModel>> =
        _foryouPromotionList

    private var _searchResultModel = MutableLiveData<SearchResultModel>()
    val searchResultModel: LiveData<SearchResultModel> = _searchResultModel

    fun setArgumentModel(searchResultModel: SearchResultModel?) {
        Timber.d("!==! SearchArguments: ${Gson().toJson(searchResultModel)}")
        searchResultModel?.let {
            _searchResultModel.value = it
        }
    }

    fun fetchCardResult() {
        if (appPreference.myCreditCards?.isNotEmpty().toDefaultValue()) {
            creditCardCollectionStore
                .whereIn(ID, appPreference.myCreditCards?.toList().toDefaultValue())
                .get()
                .addOnSuccessListener { result ->
                    val myCreditCardList = mutableListOf<CreditCardResponse>()
                    for (document in result) {
                        val creditCardResponse = document.toObject(CreditCardResponse::class.java)
                        myCreditCardList.add(creditCardResponse)
                    }

                    val categoryFilter = myCreditCardList.filter {
                        it.categoryType?.any { categorySpend ->
                            categorySpend == _searchResultModel.value?.categorySpend?.lowercase()
                        } == true && _searchResultModel.value?.estimateSpend.toDefaultValue() >= it.cashbackConditions?.firstOrNull()?.minSpend.toDefaultValue()
                    }

                    val creditCardResultModel = categoryFilter.map {
                        it.mapToCreditCardSearchResultModel(
                            _searchResultModel.value?.estimateSpend.toDefaultValue(),
                            _searchResultModel.value?.categorySpend.toDefaultValue()
                        )
                    }

                    creditCardResultModel.toMutableList().flagCashbackHighest()

                    val highestCreditCardSorted = creditCardResultModel.sortedByDescending {
                        it.cashbackEarnedBath.toDouble()
                    }

                    Timber.d("!==! CreditCard Result: ${highestCreditCardSorted}")
                    _creditCardSearchResultList.postValue(highestCreditCardSorted)
                }
                .addOnFailureListener { exception ->
                    Timber.e("Error getting documents. ${exception}")
                }
        }
    }

    fun fetchPromotion(myLocation: MyLocation?) {
        promotionCollectionStore
            .get()
            .addOnSuccessListener { result ->
                val foryouPromotionList = mutableListOf<PromotionListResponse>()
                for (document in result) {
                    val promotionListResponse =
                        document.toObject(PromotionListResponse::class.java)
                    foryouPromotionList.add(promotionListResponse)
                }

                val promotionSearchResultFilter = foryouPromotionList.filter {
                    isCategoryMatchInPromotion(
                        _searchResultModel.value?.categorySpend?.lowercase().toDefaultValue(),
                        it.categoryType.toDefaultValue()
                    ) && isMyCardRelatedInPromotion(
                        appPreference.myCreditCards?.toList().toDefaultValue(),
                        it.creditCardRelation.toDefaultValue()
                    ) && isEstimateSpendMoreThenMinSpend(
                        _searchResultModel.value?.estimateSpend.toDefaultValue(),
                        it.cashbackConditions?.firstOrNull()?.minSpend.toDefaultValue()
                    ) && isCurrentDateInRange(
                        it.startDate.toDefaultValue(), it.endDate.toDefaultValue()
                    )
                }

                // Filter by Distance or Filter by Highest Cashback
                if (myLocation != null) {
                    val promotionFilterByLocation = promotionSearchResultFilter.map {
                        it.mapToForYouPromotionModel(
                            _searchResultModel.value?.estimateSpend.toDefaultValue(),
                            calculateDistanceFromLocation(myLocation, it.location?.firstOrNull())
                        )
                    }.sortedBy {
                        it.distance
                    }
                    _foryouPromotionList.postValue(promotionFilterByLocation)
                    Timber.d("!==! promotionFilter ByLocation: ${promotionFilterByLocation}")
                } else {
                    // Sorting Highest Cashback
                    val promotionFilterByCashback = promotionSearchResultFilter.map {
                        it.mapToForYouPromotionModel(
                            _searchResultModel.value?.estimateSpend.toDefaultValue(),
                            null
                        )
                    }.sortedByDescending {
                        it.cashbackEarnedBath.toDouble()
                    }
                    _foryouPromotionList.postValue(promotionFilterByCashback)
                    Timber.d("!==! promotionFilter ByCashback: ${promotionFilterByCashback}")
                }
            }
            .addOnFailureListener { exception ->
                Timber.e("Error getting documents. ${exception}")
            }
    }

    fun isCurrentDateInRange(startDate: String, endDate: String): Boolean {
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val currentDate = Date()

        val startDateObj = formatter.parse(startDate)
        val endDateObj = formatter.parse(endDate)

        return currentDate.after(startDateObj) && currentDate.before(endDateObj)
    }

    fun isCategoryMatchInPromotion(categorySpend: String, categoryTypes: List<String>): Boolean {
        return categoryTypes.any { promotionCategory ->
            promotionCategory == categorySpend
        }
    }

    fun isMyCardRelatedInPromotion(myCards: List<String>, CardRelations: List<String>): Boolean {
        return myCards.intersect(CardRelations.toSet()).isNotEmpty()
    }

    fun isEstimateSpendMoreThenMinSpend(estimateSpend: Int, minSpend: Int): Boolean {
        return estimateSpend >= minSpend
    }

    private fun calculateDistanceFromLocation(
        myLocation: MyLocation,
        promotionLocation: PromotionLocation?
    ): Float {
        val currentLocation = Location("50 Years Faculty of Commerce and Accountancy Tower")
        currentLocation.latitude = myLocation.lat
        currentLocation.longitude = myLocation.lng

        val destinationPoint = Location("Faculty of Commerce and Accountancy (CU)")
        destinationPoint.latitude = promotionLocation?.lat ?: myLocation.lat
        destinationPoint.longitude = promotionLocation?.lng ?: myLocation.lng

        return currentLocation.distanceTo(destinationPoint)
    }
}
