package com.bsd.specialproject.ui.home

import androidx.lifecycle.*
import com.bsd.specialproject.constants.CREDIT_CARD_LIST
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.bsd.specialproject.utils.toDefaultValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class HomeViewModel(private val appPreference: AppPreference) : ViewModel() {

    private val _myCreditCardList = MutableLiveData<List<CreditCardModel>>()
    val myCreditCardList: LiveData<List<CreditCardModel>> = _myCreditCardList

    fun fetchMyCreditCard() {
        val fbStore = Firebase.firestore
        fbStore.collection(CREDIT_CARD_LIST)
            .whereIn("id", appPreference.myCreditCards?.toList().toDefaultValue())
            .get()
            .addOnSuccessListener { result ->
                val myCreditCardList = mutableListOf<CreditCardModel>()
                for (document in result) {
                    val creditCardModel = document.toObject(CreditCardModel::class.java)
                    myCreditCardList.add(creditCardModel)
                }
                _myCreditCardList.postValue(myCreditCardList)
            }
            .addOnFailureListener { exception ->
                Timber.e("Error getting documents. ${exception}")
            }
    }

}
