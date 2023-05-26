package com.bsd.specialproject.ui.addcreditcard

import androidx.lifecycle.*
import com.bsd.specialproject.constants.CREDIT_CARD_LIST
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.bsd.specialproject.utils.toDefaultValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class CreditCardViewModel(private val appPreference: AppPreference) : ViewModel() {

    private val creditCardCollectionStore by lazy {
        Firebase.firestore.collection(CREDIT_CARD_LIST)
    }

    private val myCreditCardIds by lazy {
        appPreference.myCreditCards?.toList()
    }

    private val _creditCardList = MutableLiveData<List<CreditCardModel>>()
    val creditCardList: LiveData<List<CreditCardModel>> = _creditCardList

    private val _myCardList = MutableLiveData<List<CreditCardModel>>()
    val myCardList: LiveData<List<CreditCardModel>> = _myCardList

    fun fetchMyCards() {
        if (appPreference.myCreditCards?.isNotEmpty().toDefaultValue()) {
            creditCardCollectionStore
                .whereIn("id", appPreference.myCreditCards?.toList().toDefaultValue())
                .get()
                .addOnSuccessListener { result ->
                    val myCreditCardList = mutableListOf<CreditCardModel>()
                    for (document in result) {
                        val creditCardModel = document.toObject(CreditCardModel::class.java)
                        myCreditCardList.add(creditCardModel)
                    }
                    _myCardList.postValue(myCreditCardList)
                }
                .addOnFailureListener { exception ->
                    Timber.e("Error getting documents. ${exception}")
                }
        }
    }

    fun fetchAddMoreCreditCard() {
        if (myCreditCardIds?.isNotEmpty().toDefaultValue()) {
            creditCardCollectionStore
                .whereNotIn("id", myCreditCardIds.toDefaultValue())
                .get()
                .addOnSuccessListener { result ->
                    val creditCardList = mutableListOf<CreditCardModel>()
                    for (document in result) {
                        val creditCardModel = document.toObject(CreditCardModel::class.java)
                        creditCardList.add(creditCardModel)
                    }
                    _creditCardList.postValue(creditCardList)
                }
                .addOnFailureListener { exception ->
                    Timber.e("Error getting documents. ${exception}")
                }
        } else {
            creditCardCollectionStore
                .get()
                .addOnSuccessListener { result ->
                    val creditCardList = mutableListOf<CreditCardModel>()
                    for (document in result) {
                        val creditCardModel = document.toObject(CreditCardModel::class.java)
                        creditCardList.add(creditCardModel)
                    }
                    _creditCardList.postValue(creditCardList)
                }
                .addOnFailureListener { exception ->
                    Timber.e("Error getting documents. ${exception}")
                }
        }
    }

    fun addedToMyCards(creditCardIds: List<String>) {
        val latestMyCards = mutableListOf<String>()
        latestMyCards.addAll(myCreditCardIds.toDefaultValue() + creditCardIds)
        appPreference.myCreditCards = latestMyCards.toSet()
    }

    fun removedMyCards(creditCardIds: List<String>) {
        val latestMyCards = mutableListOf<String>()
        latestMyCards.addAll(myCreditCardIds.toDefaultValue() - creditCardIds.toSet())
        appPreference.myCreditCards = latestMyCards.toSet()
    }
}
