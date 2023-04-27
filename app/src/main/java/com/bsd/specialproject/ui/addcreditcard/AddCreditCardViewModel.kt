package com.bsd.specialproject.ui.addcreditcard

import androidx.lifecycle.*
import com.bsd.specialproject.constants.CREDIT_CARD_LIST
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class AddCreditCardViewModel : ViewModel() {

    private val _creditCardList = MutableLiveData<List<String>>()
    val creditCardList: LiveData<List<String>> = _creditCardList

    fun fetchCreditCard() {
        val fbStore = Firebase.firestore
        fbStore.collection(CREDIT_CARD_LIST)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Timber.d("!==! ${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Timber.e("Error getting documents. ${exception}")
            }
    }
}
