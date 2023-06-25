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
    private val myUserCollectionStore by lazy {
        Firebase.firestore.collection(USERS).document(deviceSettings.deviceId)
    }
    private val myCardsCollectionStore by lazy {
        myUserCollectionStore.collection(MY_CARDS)
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
            fetchMyCardsFromFirebase()
        }
    }

    fun fetchAddMoreCreditCard() {
        if (myCreditCardIds?.isNotEmpty().toDefaultValue()) {
            creditCardCollectionStore
                .whereNotIn(ID, myCreditCardIds.toDefaultValue())
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
                    Timber.e("UC1-AddMoreCard Error getting documents. ${exception}")
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
                    Timber.e("UC1- AddMoreCard Error getting documents. ${exception}")
                }
        }
    }

    fun addedToMyCards(creditCardIds: List<String>) {
        val latestMyCards = mutableListOf<String>()
        latestMyCards.addAll(myCreditCardIds.toDefaultValue() + creditCardIds)
        appPreference.myCreditCards = latestMyCards.toSet()
        Timber.d("!==! UC1-AddedCard UpdateMyCreditCardIds: ${latestMyCards}")

        val myCurrentCards = _creditCardList.value?.filter {
            latestMyCards.contains(it.id.toDefaultValue())
        }.toDefaultValue()

        Timber.d("!==! UC1-AddedCard CreditCardListToAdded: ${myCurrentCards}")
        updateMyCardsToFirestore(myCurrentCards, true)
    }

    fun removedMyCards(creditCardIds: List<String>) {
        val latestMyCards = mutableListOf<String>()
        latestMyCards.addAll(myCreditCardIds.toDefaultValue() - creditCardIds.toSet())
        appPreference.myCreditCards = latestMyCards.toSet()
        Timber.d("!==! UC1-RemovedCard UpdateMyCreditCardIds: ${latestMyCards}")

        val myCurrentCards = _myCardList.value?.filter {
            !latestMyCards.contains(it.id.toDefaultValue())
        }.toDefaultValue()

        Timber.d("!==! UC1-RemovedCard CreditCardListToRemoved: ${myCurrentCards}")
        updateMyCardsToFirestore(myCurrentCards, false)
    }

    private fun updateMyCardsToFirestore(myCards: List<CreditCardResponse>, isAddedCard: Boolean) {
        myCards.forEach {
            if (isAddedCard) {
                myCardsCollectionStore.document(it.id.toDefaultValue())
                    .set(it)
                    .addOnSuccessListener {
                        Timber.d("UC1-Added CreditCard DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e ->
                        Timber.e("Error writing document", e)
                    }
            } else {
                myCardsCollectionStore.document(it.id.toDefaultValue())
                    .delete()
                    .addOnSuccessListener {
                        Timber.d("UC1-Removed CreditCard DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e ->
                        Timber.e("Error writing document", e)
                    }
            }
        }
    }

    fun fetchMyCardsFromFirebase() {
        myCardsCollectionStore.get().addOnSuccessListener {
            val myCards = it.toObjects(CreditCardResponse::class.java)
            Timber.d("!==! UC1-FetchMyCreditCards: ${myCards}")
            _myCardList.postValue(myCards)
        }.addOnFailureListener {
            Timber.e("!==! UC1-FetchMyCreditCards: ${it}")
        }
    }
}
