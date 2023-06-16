package com.bsd.specialproject.ui.searchresult

import androidx.lifecycle.*
import com.bsd.specialproject.constants.CREDIT_CARD_LIST
import com.bsd.specialproject.constants.ID
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.ui.searchresult.model.*
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.bsd.specialproject.utils.toDefaultValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import timber.log.Timber

class SearchResultViewModel(private val appPreference: AppPreference) : ViewModel() {
    private val creditCardCollectionStore by lazy {
        Firebase.firestore.collection(CREDIT_CARD_LIST)
    }

    private val myCreditCardIds by lazy {
        appPreference.myCreditCards?.toList()
    }

    private val _creditCardSearchResultList = MutableLiveData<List<CreditCardSearchResultModel>>()
    val creditCardSearchResultList: LiveData<List<CreditCardSearchResultModel>> =
        _creditCardSearchResultList

    private var _searchResultModel: SearchResultModel? = null

    fun setArgumentModel(searchResultModel: SearchResultModel?) {
        Timber.d("!==! SearchArguments: ${Gson().toJson(searchResultModel)}")
        _searchResultModel = searchResultModel
    }

    fun fetchCardResult() {
        if (appPreference.myCreditCards?.isNotEmpty().toDefaultValue()) {
            creditCardCollectionStore
                .whereIn(ID, appPreference.myCreditCards?.toList().toDefaultValue())
                .get()
                .addOnSuccessListener { result ->
                    val myCreditCardList = mutableListOf<CreditCardModel>()
                    for (document in result) {
                        val creditCardModel = document.toObject(CreditCardModel::class.java)
                        myCreditCardList.add(creditCardModel)
                    }

                    val categoryFilter = myCreditCardList.filter {
                        it.categoryType?.any { categorySpend ->
                            categorySpend == _searchResultModel?.categorySpend?.lowercase()
                        } == true && _searchResultModel?.estimateSpend.toDefaultValue() >= it.cashbackConditions?.firstOrNull()?.minSpend.toDefaultValue()
                    }

                    val creditCardResultModel = categoryFilter.map {
                        it.mapToCreditCardSearchResultModel(
                            _searchResultModel?.estimateSpend.toDefaultValue(),
                            _searchResultModel?.categorySpend.toDefaultValue()
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

    private fun List<CreditCardSearchResultModel>.flagCashbackHighest() {
        val highestBath = this.maxByOrNull { it.cashbackEarnedBath }?.cashbackEarnedBath

        this.forEachIndexed { index, creditCardSearchResultModel ->
            if (creditCardSearchResultModel.cashbackEarnedBath == highestBath) {
                creditCardSearchResultModel.isCashbackHighest = true
                creditCardSearchResultModel.indexOfCashbackHighest = index
            }
        }
    }
}
