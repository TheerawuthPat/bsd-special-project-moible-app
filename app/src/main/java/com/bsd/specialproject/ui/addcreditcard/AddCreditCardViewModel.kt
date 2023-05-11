package com.bsd.specialproject.ui.addcreditcard

import androidx.lifecycle.*
import com.bsd.specialproject.constants.CREDIT_CARD_LIST
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class AddCreditCardViewModel : ViewModel() {

    private val _creditCardList = MutableLiveData<List<CreditCardModel>>()
    val creditCardList: LiveData<List<CreditCardModel>> = _creditCardList

    fun fetchCreditCard() {
        val fbStore = Firebase.firestore
        fbStore.collection(CREDIT_CARD_LIST)
            .get()
            .addOnSuccessListener { result ->
                val creditCardList = mutableListOf<CreditCardModel>()
                for (document in result) {
                    Timber.d("!==! ${document.id} => ${document.data}")
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
