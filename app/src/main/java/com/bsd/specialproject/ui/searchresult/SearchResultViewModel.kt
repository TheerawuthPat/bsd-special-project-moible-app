package com.bsd.specialproject.ui.searchresult

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.bsd.specialproject.constants.*
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.ui.searchresult.model.*
import com.bsd.specialproject.utils.*
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class SearchResultViewModel(
    private val appPreference: AppPreference,
    private val deviceSettings: DeviceSettings
) : ViewModel() {
    private val creditCardCollectionStore by lazy {
        Firebase.firestore.collection(CREDIT_CARD_LIST)
    }
    private val promotionCollectionStore by lazy {
        Firebase.firestore.collection(PROMOTION_LIST)
    }
    private val myUserCollectionStore by lazy {
        Firebase.firestore.collection(USERS).document(deviceSettings.deviceId)
    }
    private val myPromotionCollectionStore by lazy {
        Firebase.firestore.collection(USERS).document(deviceSettings.deviceId).collection(
            MY_PROMOTION
        )
    }

    private val _creditCardSearchResultList = MutableLiveData<List<CreditCardSearchResultModel>>()
    val creditCardSearchResultList: LiveData<List<CreditCardSearchResultModel>> =
        _creditCardSearchResultList

    private val _foryouPromotionList = MutableLiveData<List<ForYouPromotionModel>>()
    val foryouPromotionList: LiveData<List<ForYouPromotionModel>> =
        _foryouPromotionList

    private var _searchResultModel = MutableLiveData<SearchResultModel>()
    val searchResultModel: LiveData<SearchResultModel> = _searchResultModel

    private var _savedToMyCards = MutableLiveData<Boolean>()
    val savedToMyCards: LiveData<Boolean> = _savedToMyCards

    private var _myPromotionList = MutableLiveData<List<MyPromotionModel>>()
    val myPromotionList: LiveData<List<MyPromotionModel>> = _myPromotionList

    private var _strategyCreditCard = MutableLiveData<List<StrategyCreditCardModel>>()
    val strategyCreditCard: LiveData<List<StrategyCreditCardModel>> = _strategyCreditCard

    private val _theBestOfCreditCard =
        MediatorLiveData<String>().apply {
            var bestOfCreditCardId = ""
            addSource(_myPromotionList) { myPromotionList ->
                if (myPromotionList.isNotEmpty()) {
//                    Timber.d("!==! MediatorLiveData MyPromotion: ${myPromotionList.firstOrNull()?.cardSelectedId.toDefaultValue()}")
                    bestOfCreditCardId =
                        myPromotionList.firstOrNull()?.cardSelectedId.toDefaultValue()
                    value = bestOfCreditCardId
                }
            }
            addSource(_creditCardSearchResultList) { creditCardResultList ->
                if (creditCardResultList.isNotEmpty() && bestOfCreditCardId.isEmpty()) {
//                    Timber.d("!==! MediatorLiveData CardResult: ${creditCardResultList.firstOrNull()?.id.toDefaultValue()}")
                    bestOfCreditCardId = creditCardResultList.firstOrNull()?.id.toDefaultValue()
                    value = bestOfCreditCardId
                }
            }
            addSource(_foryouPromotionList) { foryouPromotionList ->
                if (foryouPromotionList.isNotEmpty() && bestOfCreditCardId.isEmpty()) {
//                    Timber.d("!==! MediatorLiveData ForYou: ${foryouPromotionList.firstOrNull()?.creditCardRelation?.firstOrNull()}")
                    bestOfCreditCardId =
                        foryouPromotionList.firstOrNull()?.creditCardRelation?.firstOrNull()
                            .toDefaultValue()
                    value = bestOfCreditCardId
                }
            }
        }
    val theBestOfCreditCard = _theBestOfCreditCard

    fun setArgumentModel(searchResultModel: SearchResultModel?) {
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

                    Timber.d("!==! CreditCardResult: ${highestCreditCardSorted}")
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
//                    Timber.d("!==! promotionFilter ByLocation: ${promotionFilterByLocation}")
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
//                    Timber.d("!==! promotionFilter ByCashback: ${promotionFilterByCashback}")
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun savedToMyPromotion(
        promotionModel: ForYouPromotionModel,
        cardSelectedId: String,
        cardSelectedName: String
    ) {
        val myPromotionUpdatedModel = if (_myPromotionList.value?.isNotEmpty() == true) {
            val isHaveThisPromotion = _myPromotionList.value?.any {
                it.id == promotionModel.id
            }
            if (isHaveThisPromotion.toDefaultValue()) {
                val currentMyPromotion = _myPromotionList.value?.single {
                    it.id == promotionModel.id
                }
                val currentEstimateSpend = _searchResultModel.value?.estimateSpend.toDefaultValue()
                val currentAccumulateSpend =
                    currentMyPromotion?.accumulateSpend?.toDefaultValue()
                        ?.plus(currentEstimateSpend)
                promotionModel.mapToMyPromotion(
                    currentAccumulateSpend.toDefaultValue(),
                    cardSelectedId,
                    cardSelectedName,
                    currentMyPromotion?.savedDate
                )
            } else {
                promotionModel.mapToMyPromotion(
                    _searchResultModel.value?.estimateSpend.toDefaultValue(),
                    cardSelectedId,
                    cardSelectedName
                )
            }
        } else {
            promotionModel.mapToMyPromotion(
                _searchResultModel.value?.estimateSpend.toDefaultValue(),
                cardSelectedId,
                cardSelectedName
            )
        }
        myUserCollectionStore.collection(MY_PROMOTION)
            .document(myPromotionUpdatedModel.id.toDefaultValue() + "_" + cardSelectedId)
            .set(myPromotionUpdatedModel)
            .addOnSuccessListener {
                _savedToMyCards.postValue(true)
                Timber.e("Users DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Timber.e("Error writing document", e)
            }
    }

    fun fetchMyPromotion() {
        myPromotionCollectionStore.get().addOnSuccessListener {
            val myPromotionListModel = it.toObjects(MyPromotionModel::class.java)

            val myPromotionFilter = myPromotionListModel.filter {
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

            val myPromotion = myPromotionFilter.map {
                val totalMoreSpend =
                    _searchResultModel.value?.estimateSpend.toDefaultValue() + it.moreAccumulateSpend.toDefaultValue()
                if (_searchResultModel.value?.estimateSpend.toDefaultValue() >= it.moreAccumulateSpend.toDefaultValue()) {
                    it.criteria = 1
                } else {
                    it.criteria = 2
                }
                it.moreCashbackPercent =
                    it.cashbackConditions?.getCashbackPerTime(totalMoreSpend).toDefaultValue()
                it
            }

            //check criteria count
            val firstCriteriaCount = myPromotion.count { it.criteria == 1 }
            val secondCriteriaCount = myPromotion.count { it.criteria == 2 }

            //sorting by criteria
            val myPromotionSorted = if (firstCriteriaCount >= secondCriteriaCount) {
                myPromotion.sortedByDescending {
                    it.cashbackEarnedBath
                }
            } else {
                myPromotion.sortedBy {
                    it.remainingDate?.toInt()
                }
            }

            _myPromotionList.postValue(myPromotionSorted)
        }
    }

    fun fetchStrategyCreditCard(isSpitBill: Boolean) {
        //mockup data
        var fullBillModel: StrategyCreditCardModel.FullBillModel =
            StrategyCreditCardModel.FullBillModel(
                "",
                emptyList(),
                emptyList(),
                0,
                "",
                ""
            )
        var splitBillModel = StrategyCreditCardModel.SplitBillModel(
            "",
            emptyList(),
            emptyList(),
            0,
            "",
            ""
        )

        // step1: check estimateSpend > or < myExpenseLastMonth
        val estimateSpend = _searchResultModel.value?.estimateSpend.toDefaultValue()
        val myExpenseLastMonth = appPreference.myExpenseLastMonth
        var balanceSpendOfMonth = 0

        val mustToSpendOfMonth = if (myExpenseLastMonth.isNotEmpty()) {
            if (estimateSpend > myExpenseLastMonth.toInt()) {
                estimateSpend
            } else {
                balanceSpendOfMonth = myExpenseLastMonth.toInt() - estimateSpend
                appPreference.myExpenseLastMonth.toInt()
            }
        } else {
            estimateSpend
        }
        Timber.d("!==! UC5-MustToSpendOfMonth: ${mustToSpendOfMonth}")
        Timber.d("!==! UC5-MyExpenseLastMonth: ${appPreference.myExpenseLastMonth.toInt()}")

        //step2: calculate cashbackEarned of mustToSpend
        val estimateCashbackPerMonthOfCreditCard = _creditCardSearchResultList.value?.map {
            it.mapToStrategySearchResultModel(estimateSpend)
        }?.map {
            //step3: check cashbackEarned < limitCashbackPerMonth and assign value
            if (it.cashbackEarnedBathPerMonth > it.limitCashbackPerMonth.toDouble()) {
                it.cashbackEarnedBathPerMonth = it.limitCashbackPerMonth.toDouble()
            }
            Timber.d("!==! UC5-cashbackEarnedBathPerMonth: ${it.name}-${it.cashbackEarnedBathPerMonth}")
            it
        }
        Timber.d("!==! UC5-EstimateCashbackPerMonthOfCreditCard: ${estimateCashbackPerMonthOfCreditCard}")

        //step4: Mapping data to FullBill and SplitBill
        if (isSpitBill) {
            // SpitBill
            val mustCreditCards = mutableListOf<MustCreditCardSpendModel>()
            val balanceCreditCards = mutableListOf<BalanceCreditCardSpendModel>()
            var totalCashback = 0


        } else {
            // FullBill
            val mustCreditCards = mutableListOf<MustCreditCardSpendModel>()
            val balanceCreditCards = mutableListOf<BalanceCreditCardSpendModel>()
            var totalCashback = 0

            if (balanceSpendOfMonth == 0) {
                estimateCashbackPerMonthOfCreditCard?.first()
                    ?.mapToMustCreditCardSpendModel(estimateSpend)?.let {
                        mustCreditCards.add(it)
                    }
            } else {
                var firstBalanceCardResult: BalanceCreditCardSpendModel
                var nextBalanceCardResult: BalanceCreditCardSpendModel
                var balanceSpending = 0
                var balanceCashbackEarned = 0

                estimateCashbackPerMonthOfCreditCard?.forEachIndexed { index, strategySearchResultModel ->
                    when (index) {
                        0 -> {
                            val firstMustCardResult =
                                strategySearchResultModel.mapToMustCreditCardSpendModel(
                                    estimateSpend
                                )
                            mustCreditCards.add(firstMustCardResult)

                            if (firstMustCardResult.cashbackEarned < strategySearchResultModel.limitCashbackPerMonth) {
                                balanceCashbackEarned = firstMustCardResult.cashbackEarned
                                val balanceSpendForMaximumCashback =
                                    strategySearchResultModel.maximumSpendForCashback - estimateSpend

                                //add to first balance items
                                firstBalanceCardResult =
                                    strategySearchResultModel.mapToBalanceCreditCardSpendModel(
                                        balanceSpendOfMonth,
                                        balanceCashbackEarned,
                                        balanceSpendForMaximumCashback
                                    )
                                balanceCreditCards.add(firstBalanceCardResult)

                                balanceSpending =
                                    balanceSpendOfMonth - balanceSpendForMaximumCashback

                                Timber.d("!==! UC5-firstBalanceCardResult-name${firstBalanceCardResult.creditCardName}")
                                Timber.d("!==! UC5-firstBalanceCardResult-balanceSpending: ${balanceSpending}")
                                Timber.d("!==! UC5-firstBalanceCardResult-cashbackEarned: ${firstBalanceCardResult.cashbackEarned}")
                            } else {
                                balanceSpending = balanceSpendOfMonth
                            }
                        }

                        else -> {
                            if (balanceSpending >= strategySearchResultModel.maximumSpendForCashback) {
                                nextBalanceCardResult =
                                    strategySearchResultModel.mapToBalanceCreditCardSpendModel(
                                        strategySearchResultModel.maximumSpendForCashback,
                                        null,
                                        strategySearchResultModel.maximumSpendForCashback
                                    )
                            } else {
                                nextBalanceCardResult =
                                    strategySearchResultModel.mapToBalanceCreditCardSpendModel(
                                        balanceSpending,
                                        null,
                                        balanceSpending
                                    )
                            }
                            if (balanceSpending != 0) {
                                balanceCreditCards.add(nextBalanceCardResult)
                                balanceSpending -= nextBalanceCardResult.balanceSpendOfMonth
                            }
                            Timber.d("!==! UC5-nextBalanceCardResult-name${nextBalanceCardResult.creditCardName}")
                            Timber.d("!==! UC5-nextBalanceCardResult-balanceSpending: ${balanceSpending}")
                            Timber.d("!==! UC5-nextBalanceCardResult-cashbackEarned: ${nextBalanceCardResult.cashbackEarned}")
                        }
                    }
                }
            }

            //step5: calculate total cashback
            mustCreditCards.forEach {
                totalCashback += it.cashbackEarned
            }
            balanceCreditCards.forEach {
                totalCashback += it.cashbackEarned
            }

            Timber.d("!==! UC5-mustCreditCards: ${mustCreditCards}")
            Timber.d("!==! UC5-balanceCreditCards: ${balanceCreditCards}")
            Timber.d("!==! UC5-TotalCashback: ${totalCashback}")

            //map model to show
            fullBillModel = StrategyCreditCardModel.FullBillModel(
                estimateSpend = estimateSpend.toString(),
                mustCreditCardSpends = mustCreditCards,
                balanceCreditCardSpends = balanceCreditCards,
                totalCashback = totalCashback,
                myExpenseLastMonth = myExpenseLastMonth,
                balanceSpend = balanceSpendOfMonth.toString()
            )
        }

        _strategyCreditCard.postValue(
            listOf(
                if (isSpitBill) {
                    splitBillModel
                } else {
                    fullBillModel
                }
            )
        )
    }
}
