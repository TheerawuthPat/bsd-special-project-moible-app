package com.bsd.specialproject.ui.addcreditcard

import androidx.lifecycle.*
import com.bsd.specialproject.constants.*
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.utils.DeviceSettings
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.bsd.specialproject.utils.toDefaultValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class CreditCardViewModel(
    private val appPreference: AppPreference,
    private val deviceSettings: DeviceSettings
) : ViewModel() {

    private val creditCardCollectionStore by lazy {
        Firebase.firestore.collection(CREDIT_CARD_LIST)
    }
    private val usersCollectionStore by lazy {
        Firebase.firestore.collection(USERS).document(deviceSettings.deviceId)
    }

    private val myCreditCardIds by lazy {
        appPreference.myCreditCards?.toList()
    }

    private val _creditCardList = MutableLiveData<List<CreditCardResponse>>()
    val creditCardList: LiveData<List<CreditCardResponse>> = _creditCardList

    private val _myCardList = MutableLiveData<List<CreditCardResponse>>()
    val myCardList: LiveData<List<CreditCardResponse>> = _myCardList

    fun fetchMyCards() {
        if (appPreference.myCreditCards?.isNotEmpty().toDefaultValue()) {
            creditCardCollectionStore
                .whereIn("id", appPreference.myCreditCards?.toList().toDefaultValue())
                .get()
                .addOnSuccessListener { result ->
                    val myCreditCardList = mutableListOf<CreditCardResponse>()
                    for (document in result) {
                        val creditCardResponse = document.toObject(CreditCardResponse::class.java)
                        myCreditCardList.add(creditCardResponse)
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
                    val creditCardList = mutableListOf<CreditCardResponse>()
                    for (document in result) {
                        val creditCardResponse = document.toObject(CreditCardResponse::class.java)
                        creditCardList.add(creditCardResponse)
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
                    val creditCardList = mutableListOf<CreditCardResponse>()
                    for (document in result) {
                        val creditCardResponse = document.toObject(CreditCardResponse::class.java)
                        creditCardList.add(creditCardResponse)
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
        updateMyCardsToFirestore(latestMyCards)
    }

    fun removedMyCards(creditCardIds: List<String>) {
        val latestMyCards = mutableListOf<String>()
        latestMyCards.addAll(myCreditCardIds.toDefaultValue() - creditCardIds.toSet())
        appPreference.myCreditCards = latestMyCards.toSet()
        updateMyCardsToFirestore(latestMyCards)
    }

    private fun updateMyCardsToFirestore(myCards: List<String>) {
        val myCreditCard = hashMapOf(
            MY_CARDS to myCards
        )
        usersCollectionStore.set(myCreditCard)
            .addOnSuccessListener {
                Timber.e("Users DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Timber.e("Error writing document", e)
            }
    }
}
