package com.bsd.specialproject.ui.promotion

import androidx.lifecycle.*
import com.bsd.specialproject.constants.MY_PROMOTION
import com.bsd.specialproject.constants.USERS
import com.bsd.specialproject.ui.searchresult.model.MyPromotionModel
import com.bsd.specialproject.utils.*
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPromotionViewModel(
    private val appPreference: AppPreference,
    private val deviceSettings: DeviceSettings
) : ViewModel() {

    private val myPromotionCollectionStore by lazy {
        Firebase.firestore.collection(USERS).document(deviceSettings.deviceId).collection(
            MY_PROMOTION
        )
    }

    private var _myPromotionList = MutableLiveData<List<MyPromotionModel>>()
    val myPromotionList: LiveData<List<MyPromotionModel>> = _myPromotionList

    fun fetchMyPromotion() {
        myPromotionCollectionStore.get().addOnSuccessListener {
            val myPromotionListModel = it.toObjects(MyPromotionModel::class.java)


            val myPromotion = myPromotionListModel.map {
                it.moreCashbackPercent =
                    it.cashbackConditions?.getCashbackPerTime(it.accumulateSpend.toDefaultValue())
                        .toDefaultValue()
                it
            }

            _myPromotionList.postValue(myPromotion)
        }
    }
}
