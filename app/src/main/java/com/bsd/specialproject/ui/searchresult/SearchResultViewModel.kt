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

    private val promotionCollectionStore by lazy {
        Firebase.firestore.collection(PROMOTION_LIST)
    }
    private val myUserCollectionStore by lazy {
        Firebase.firestore.collection(USERS).document(deviceSettings.deviceId)
    }
    private val myCardsCollectionStore by lazy {
        myUserCollectionStore.collection(MY_CARDS)
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

    private var _savedToMyPromotion = MutableLiveData<Boolean>()
    val savedToMyPromotion: LiveData<Boolean> = _savedToMyPromotion

    private var _savedToMyCard = MutableLiveData<Boolean>()
    val savedToMyCard: LiveData<Boolean> = _savedToMyCard

    private var _myPromotionList = MutableLiveData<List<MyPromotionModel>>()
    val myPromotionList: LiveData<List<MyPromotionModel>> = _myPromotionList

    private var _strategyCreditCard = MutableLiveData<List<StrategyCreditCardModel>>()
    val strategyCreditCard: LiveData<List<StrategyCreditCardModel>> = _strategyCreditCard

    private val _theBestOfCreditCard =
        MediatorLiveData<String>().apply {
            var bestOfCreditCardId = ""
            addSource(_myPromotionList) { myPromotionList ->
                if (myPromotionList.isNotEmpty()) {
                    Timber.d("!==! UC2 BestOfCreditCard-MyPromotion from UC2.2: ${myPromotionList.firstOrNull()?.cardSelectedId.toDefaultValue()}")
                    bestOfCreditCardId =
                        myPromotionList.firstOrNull()?.cardSelectedId.toDefaultValue()
                    value = bestOfCreditCardId
                }
            }
            addSource(_creditCardSearchResultList) { creditCardResultList ->
                if (creditCardResultList.isNotEmpty() && bestOfCreditCardId.isEmpty()) {
                    Timber.d("!==! UC2 BestOfCreditCard-CardBenefit from UC2.1: ${creditCardResultList.firstOrNull()?.id.toDefaultValue()}")
                    bestOfCreditCardId = creditCardResultList.firstOrNull()?.id.toDefaultValue()
                    value = bestOfCreditCardId
                }
            }
            addSource(_foryouPromotionList) { foryouPromotionList ->
                if (foryouPromotionList.isNotEmpty() && bestOfCreditCardId.isEmpty()) {
                    Timber.d(
                        "!==! UC2 BestOfCreditCard-ForYouPromotion from UC2.3: ${
                            foryouPromotionList.firstOrNull()?.creditCardRelation?.firstOrNull()
                                .toDefaultValue()
                        }"
                    )
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

    fun fetchCardBenefitResult() {
        if (appPreference.myCreditCards?.isNotEmpty().toDefaultValue()) {
            myCardsCollectionStore.get().addOnSuccessListener {
                val myCreditCardList = it.toObjects(CreditCardResponse::class.java)
                Timber.d("!==! UC2.3-CardBenefitResult: ${myCreditCardList}")

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
                Timber.d("!==! UC2.3-CardBenefitResult SortedHighestCashback: ${highestCreditCardSorted}")

                _creditCardSearchResultList.postValue(highestCreditCardSorted)
            }.addOnFailureListener {
                Timber.e("!==! UC2.3-CardBenefitResult Error: ${it}")
            }
        }
    }

    fun fetchForYouPromotion(myLocation: MyLocation?) {
        promotionCollectionStore
            .get()
            .addOnSuccessListener { result ->
                val foryouPromotionList = mutableListOf<PromotionListResponse>()
                for (document in result) {
                    val promotionListResponse =
                        document.toObject(PromotionListResponse::class.java)
                    foryouPromotionList.add(promotionListResponse)
                }
                Timber.d("!==! UC2.1: AllPromotion: ${foryouPromotionList.map { it.name }}")

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
                Timber.d("!==! UC2.1: FilterPromotionByBusinessRule: ${promotionSearchResultFilter.map { it.name }}")

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
                    Timber.d("!==! UC2.1: PromotionByLocation SortedByDistance: ${promotionFilterByLocation.map { it.name }}")
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
                    Timber.d("!==! UC2.1: PromotionByCashback SortedByCashbackEarned: ${promotionFilterByCashback.map { it.name }}")
                }
            }
            .addOnFailureListener { exception ->
                Timber.e("!==! UC2.1: ForYou Promotion Error getting documents. ${exception}")
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
                Timber.d("!==! UC4: AddToMyPromotion-IsExistPromotion: ${isHaveThisPromotion}")
                val currentMyPromotion = _myPromotionList.value?.single {
                    it.id == promotionModel.id
                }
                val currentEstimateSpend = _searchResultModel.value?.estimateSpend.toDefaultValue()
                val currentAccumulateSpend =
                    currentMyPromotion?.accumulateSpend?.toDefaultValue()
                        ?.plus(currentEstimateSpend)
                Timber.d("!==! UC4: AddToMyPromotion-UpdateAccumulateSpend: ${currentAccumulateSpend}")
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
        Timber.d("!==! UC4: AddToMyPromotion: ${myPromotionUpdatedModel}")
        myUserCollectionStore.collection(MY_PROMOTION)
            .document(myPromotionUpdatedModel.id.toDefaultValue() + "_" + cardSelectedId)
            .set(myPromotionUpdatedModel)
            .addOnSuccessListener {
                _savedToMyPromotion.postValue(true)
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
                Timber.d("!==! UC2.2: MyPromotion-MoreAccumulateSpend: ${it.moreAccumulateSpend}")
                Timber.d("!==! UC2.2: MyPromotion-MoreCashbackPercent: ${it.moreCashbackPercent}")
                it
            }
            Timber.d("!==! UC2.2: MyPromotion-Criteria: ${myPromotionFilter.map { it.criteria }}")

            //check criteria count
            val firstCriteriaCount = myPromotion.count { it.criteria == 1 }
            val secondCriteriaCount = myPromotion.count { it.criteria == 2 }
            Timber.d("!==! UC2.2: MyPromotion-Count Criteria1: ${firstCriteriaCount}")
            Timber.d("!==! UC2.2: MyPromotion-Count Criteria2: ${secondCriteriaCount}")

            //sorting by criteria
            val myPromotionSorted = if (firstCriteriaCount >= secondCriteriaCount) {
                val sortedByCashback = myPromotion.sortedByDescending {
                    it.cashbackEarnedBath
                }
                Timber.d("!==! UC2.2: MyPromotion-SortedByCashbackEarned: ${sortedByCashback}")
                sortedByCashback
            } else {
                val sortedByRemainingDate = myPromotion.sortedBy {
                    it.remainingDate?.toInt()
                }
                Timber.d("!==! UC2.2: MyPromotion-SortedByRemainingDate: ${sortedByRemainingDate}")
                sortedByRemainingDate
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
        var estimateSpend = _searchResultModel.value?.estimateSpend.toDefaultValue()
        val myExpenseLastMonth = appPreference.myExpenseLastMonth.ifEmpty {
            "0"
        }.toInt()
        var balanceSpendOfMonth = if (estimateSpend >= myExpenseLastMonth) {
            0
        } else {
            myExpenseLastMonth - estimateSpend
        }
        Timber.d("!==! UC5-Init EstimateSpend: ${estimateSpend}")
        Timber.d("!==! UC5-Calculate BalanceSpendOfMonth: ${balanceSpendOfMonth}")

        //step2: calculate cashbackEarned of mustToSpend
        val estimateCashbackPerMonthOfCreditCard = _creditCardSearchResultList.value?.map {
            it.mapToStrategySearchResultModel(estimateSpend)
        }?.sortedByDescending {
            it.cashbackPercent
        }
        Timber.d("!==! UC5-SortedCashbackPercent_CreditCardList: ${estimateCashbackPerMonthOfCreditCard?.map { it.name }}")

        //step4: Mapping data to FullBill and SplitBill
        if (isSpitBill) {
            // SplitBill
            Timber.d("!==! UC-5 Start Calculate SplitBill")
            val mustCreditCards = mutableListOf<MustCreditCardSpendModel>()
            val balanceCreditCards = mutableListOf<BalanceCreditCardSpendModel>()
            var totalCashback = 0
            var balanceSpending = 0
            var balanceMustSpending = 0
            var balanceCashbackEarned = 0
            var mustCardResult: MustCreditCardSpendModel
            var firstBalanceCardResult: BalanceCreditCardSpendModel
            var nextBalanceCardResult: BalanceCreditCardSpendModel

            if (balanceSpendOfMonth == 0) {
                Timber.d("!==! UC5-SplitBill Case1: EstimateSpend > MyExpenseLastMonth")
                Timber.d("!==! UC5-SplitBill Check AccumulateCashback Each Cards")
                estimateCashbackPerMonthOfCreditCard?.forEach { strategySearchResultModel ->
                    if (strategySearchResultModel.accumulateCashback == strategySearchResultModel.limitCashbackPerMonth) {
                        // SplitBill This Card Cannot Earned Cashback
                        return@forEach
                    } else {
                        // SplitBill Continue To Next Card
                        val reCalculateMaximumSpendForCashback =
                            strategySearchResultModel.limitCashbackPerMonth.toDefaultValue()
                                .calculateSpendingForCashback(
                                    strategySearchResultModel.cashbackConditions.getCashbackPerTime(
                                        if (mustCreditCards.isEmpty()) {
                                            estimateSpend
                                        } else {
                                            balanceMustSpending
                                        }
                                    ).toDefaultValue()
                                )
                        val numberForCalculateCashback =
                            if (estimateSpend >= reCalculateMaximumSpendForCashback) {
                                // Check CashbackType is Percent or Step
                                reCalculateMaximumSpendForCashback
                            } else {
                                if (balanceMustSpending != 0) {
                                    balanceMustSpending
                                } else {
                                    estimateSpend
                                }
                            }
                        Timber.d("!==! UC5-SplitBill CreditCardName = ${strategySearchResultModel.name}")
                        Timber.d("!==! UC5-SplitBill numberForCalculateCashback = ${numberForCalculateCashback}")
                        mustCardResult =
                            strategySearchResultModel.mapToMustCreditCardSpendModel(
                                numberForCalculateCashback,
                                null,
                                numberForCalculateCashback
                            )
                        mustCreditCards.add(mustCardResult)
                        // Update BalanceMustSpending
                        if (estimateSpend >= reCalculateMaximumSpendForCashback) {
                            balanceMustSpending = if (balanceMustSpending != 0) {
                                Timber.d("!==! UC5-SplitBill UpdatingBalance NextTime = ${balanceMustSpending}")
                                balanceMustSpending
                            } else {
                                Timber.d("!==! UC5-SplitBill UpdatingBalance FirstTime = ${balanceMustSpending}")
                                estimateSpend
                            } - reCalculateMaximumSpendForCashback
                        }
                        Timber.d("!==! UC5-SplitBill Updating BalanceSpendForNextCard = ${balanceMustSpending}")
                    }
                }
            } else {
                Timber.d("!==! UC5-SplitBill Case2: EstimateSpend < MyExpenseLastMonth")
                Timber.d("!==! UC5-SplitBill Case2: EstimateSpend: ${estimateSpend}")
                Timber.d("!==! UC5-SplitBill Case2: BalanceSpendOfMonth: ${balanceSpendOfMonth}")
                estimateCashbackPerMonthOfCreditCard?.forEachIndexed { index, strategySearchResultModel ->
                    when (index) {
                        0 -> {
                            if (strategySearchResultModel.accumulateCashback == strategySearchResultModel.limitCashbackPerMonth) {
                                balanceMustSpending = estimateSpend
                                return@forEachIndexed
                            }
                            val reCalculateMaximumSpendForCashback =
                                strategySearchResultModel.limitCashbackPerMonth.toDefaultValue()
                                    .calculateSpendingForCashback(
                                        strategySearchResultModel.cashbackConditions.getCashbackPerTime(
                                            estimateSpend
                                        ).toDefaultValue()
                                    )
                            val numberForCalculateCashback =
                                if (estimateSpend >= reCalculateMaximumSpendForCashback) {
                                    reCalculateMaximumSpendForCashback
                                } else {
                                    estimateSpend
                                }
                            Timber.d("!==! UC5-SplitBill CreditCardName = ${strategySearchResultModel.name}")
                            Timber.d("!==! UC5-SplitBill numberForCalculateCashback = ${numberForCalculateCashback}")
                            if (estimateSpend >= reCalculateMaximumSpendForCashback) {
                                // Continues Calculate Next Must Card
                                val firstMustCardResult =
                                    strategySearchResultModel.mapToMustCreditCardSpendModel(
                                        reCalculateMaximumSpendForCashback,
                                        null,
                                        reCalculateMaximumSpendForCashback
                                    )
                                mustCreditCards.add(firstMustCardResult)
                                balanceMustSpending =
                                    estimateSpend - reCalculateMaximumSpendForCashback
                                Timber.d("!==! UC5-SplitBill balanceMustSpending > maximumSpend: ${balanceMustSpending}")
                            } else {
                                // End of First Must Card
                                val firstMustCardResult =
                                    strategySearchResultModel.mapToMustCreditCardSpendModel(
                                        estimateSpend,
                                        null,
                                        estimateSpend
                                    )
                                mustCreditCards.add(firstMustCardResult)
                                balanceMustSpending -= estimateSpend
                                Timber.d("!==! UC5-SplitBill balanceMustSpending < maximumSpend: ${balanceMustSpending}")
                            }
                        }

                        else -> {
                            Timber.d("!==! UC5-SplitBill NextCard After FirstCard")
                            if (mustCreditCards.isEmpty()) {
                                val reCalculateMaximumSpendForCashback =
                                    strategySearchResultModel.limitCashbackPerMonth.toDefaultValue()
                                        .calculateSpendingForCashback(
                                            strategySearchResultModel.cashbackConditions.getCashbackPerTime(
                                                balanceMustSpending
                                            ).toDefaultValue()
                                        )
                                val numberForCalculateCashback =
                                    if (balanceMustSpending >= reCalculateMaximumSpendForCashback) {
                                        // Check CashbackType is Percent or Step
                                        reCalculateMaximumSpendForCashback
                                    } else {
                                        balanceMustSpending
                                    }

                                Timber.d("!==! UC5-SplitBill NextCard CreditCardName = ${strategySearchResultModel.name}")
                                Timber.d("!==! UC5-SplitBill NextCard numberForCalculateCashback = ${numberForCalculateCashback}")
                                Timber.d("!==! UC5-SplitBill NextCard reCalculateMaximumSpendForCashback = ${reCalculateMaximumSpendForCashback}")

                                mustCardResult =
                                    strategySearchResultModel.mapToMustCreditCardSpendModel(
                                        numberForCalculateCashback,
                                        null,
                                        numberForCalculateCashback
                                    )
                                mustCreditCards.add(mustCardResult)

                                // Update BalanceMustSpending
                                if (balanceMustSpending >= reCalculateMaximumSpendForCashback) {
                                    balanceMustSpending -= reCalculateMaximumSpendForCashback
                                    Timber.d("!==! UC5-SplitBill UpdatingBalance NextTime = ${balanceMustSpending}")
                                } else {
//                                    balanceMustSpending = 0
                                }
                                Timber.d("!==! UC5-SplitBill Updating BalanceSpendForNextCard = ${balanceMustSpending}")

                                if (mustCardResult.cashbackEarned < strategySearchResultModel.limitCashbackPerMonth) {
                                    balanceSpending = balanceSpendOfMonth
                                    balanceCashbackEarned = mustCardResult.cashbackEarned
                                    val reCalculateMaximumSpendForCashback =
                                        strategySearchResultModel.limitCashbackPerMonth.toDefaultValue()
                                            .calculateSpendingForCashback(
                                                strategySearchResultModel.cashbackConditions.getCashbackPerTime(
                                                    balanceMustSpending
                                                ).toDefaultValue()
                                            )
                                    val balanceSpendForMaximumCashback =
                                        reCalculateMaximumSpendForCashback - mustCardResult.installmentSpend

                                    Timber.d("!==! UC5-SplitBill firstBalanceCardResult-reCalculateMaximumSpendForCashback ${reCalculateMaximumSpendForCashback}")
                                    Timber.d("!==! UC5-SplitBill firstBalanceCardResult-balanceSpendForMaximumCashback ${balanceSpendForMaximumCashback}")
                                    //add to first balance items
                                    firstBalanceCardResult =
                                        strategySearchResultModel.mapToBalanceCreditCardSpendModel(
                                            balanceSpendForMaximumCashback,
                                            null,
                                            balanceSpendForMaximumCashback
                                        )
                                    balanceCreditCards.add(firstBalanceCardResult)

                                    balanceSpending =
                                        balanceSpendOfMonth - balanceSpendForMaximumCashback
                                    Timber.d("!==! UC5-SplitBill UpdatingBalanceSpending Next BalanceCard = ${balanceSpending}")
                                }
                            } else {
                                Timber.d("!==! UC5-SplitBill: Continues Calculate Next Balance Credit Card")
                                balanceSpending = if (balanceSpending == 0) {
                                    balanceSpendOfMonth
                                } else {
                                    balanceSpending
                                }
                                Timber.d("!==! UC5-SplitBill-balanceSpendOfMonth: ${balanceSpendOfMonth}")
                                Timber.d("!==! UC5-SplitBill-balanceSpending: ${balanceSpending}")

                                val reCalculateMaximumSpendForCashback =
                                    strategySearchResultModel.limitCashbackPerMonth.toDefaultValue()
                                        .calculateSpendingForCashback(
                                            strategySearchResultModel.cashbackConditions.getCashbackPerTime(
                                                balanceSpending
                                            ).toDefaultValue()
                                        )
                                Timber.d("!==! UC5-SplitBill-MaximumSpendForCashback: ${reCalculateMaximumSpendForCashback}")

                                nextBalanceCardResult =
                                    if (balanceSpending >= reCalculateMaximumSpendForCashback) {
                                        strategySearchResultModel.mapToBalanceCreditCardSpendModel(
                                            reCalculateMaximumSpendForCashback,
                                            null,
                                            reCalculateMaximumSpendForCashback
                                        )
                                    } else {
                                        strategySearchResultModel.mapToBalanceCreditCardSpendModel(
                                            balanceSpending,
                                            null,
                                            balanceSpending
                                        )
                                    }
                                balanceCreditCards.add(nextBalanceCardResult)
                                balanceSpending -= nextBalanceCardResult.balanceSpendOfMonth
                                Timber.d("!==! UC5-SplitBill nextBalanceCardResult-name${nextBalanceCardResult.creditCardName}")
                                Timber.d("!==! UC5-SplitBill nextBalanceCardResult-balanceSpending: ${balanceSpending}")
                                Timber.d("!==! UC5-SplitBill nextBalanceCardResult-cashbackEarned: ${nextBalanceCardResult.cashbackEarned}")
                            }
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

            Timber.d("!==! UC5-SplitBill MustCreditCards: ${mustCreditCards}")
            Timber.d("!==! UC5-SplitBill BalanceCreditCards: ${balanceCreditCards}")
            Timber.d("!==! UC5-SplitBill TotalCashback: ${totalCashback}")

            //map model to show
            splitBillModel = StrategyCreditCardModel.SplitBillModel(
                estimateSpend = estimateSpend.toString(),
                mustCreditCardSpends = mustCreditCards,
                balanceCreditCardSpends = balanceCreditCards,
                totalCashback = totalCashback,
                myExpenseLastMonth = myExpenseLastMonth.toString(),
                balanceSpend = balanceSpendOfMonth.toString()
            )
        } else {
            Timber.d("!==! UC-5 Start Calculate FullBill")
            val mustCreditCards = mutableListOf<MustCreditCardSpendModel>()
            val balanceCreditCards = mutableListOf<BalanceCreditCardSpendModel>()
            var totalCashback = 0

            if (balanceSpendOfMonth == 0) {
                Timber.d("!==! UC5-FullBill Case1: EstimateSpend > MyExpenseLastMonth")
                Timber.d("!==! UC5-FullBill Check AccumulateCashback Each Cards")
                estimateCashbackPerMonthOfCreditCard?.forEach {
                    if (it.accumulateCashback == it.limitCashbackPerMonth) {
                        // FullBill This Card Cannot Earned Cashback
                        return@forEach
                    } else {
                        // FullBill Continue To Next Card
                        val reCalculateMaximumSpendForCashback =
                            it.limitCashbackPerMonth.toDefaultValue()
                                .calculateSpendingForCashback(
                                    it.cashbackConditions.getCashbackPerTime(
                                        estimateSpend
                                    ).toDefaultValue()
                                )
                        Timber.d("!==! UC5-estimateSpend: ${estimateSpend}")
                        Timber.d("!==! UC5-it.cashbackConditions: ${it.cashbackConditions}")
                        Timber.d(
                            "!==! UC5-getCashbackPerTime: ${
                                it.cashbackConditions.getCashbackPerTime(
                                    estimateSpend
                                ).toDefaultValue()
                            }"
                        )
                        it.mapToMustCreditCardSpendModel(
                            estimateSpend,
                            null,
                            reCalculateMaximumSpendForCashback
                        ).let {
                            // FullBill End of Full Bill MustCreditCard
                            if (mustCreditCards.size == 0) {
                                mustCreditCards.add(it)
                            }
                            return@forEach
                        }
                    }
                }
            } else {
                Timber.d("!==! UC5-FullBill Case2: EstimateSpend < MyExpenseLastMonth")
                Timber.d("!==! UC5-FullBill Case2: BalanceSpendOfMonth: ${balanceSpendOfMonth}")
                var firstMustCardResult: MustCreditCardSpendModel
                var firstBalanceCardResult: BalanceCreditCardSpendModel
                var nextBalanceCardResult: BalanceCreditCardSpendModel
                var balanceSpending = 0
                var balanceCashbackEarned = 0

                estimateCashbackPerMonthOfCreditCard?.forEachIndexed { index, strategySearchResultModel ->
                    when (index) {
                        0 -> {
                            if (strategySearchResultModel.accumulateCashback == strategySearchResultModel.limitCashbackPerMonth) {
                                return@forEachIndexed
                            }
                            // Map To FirstMustCreditCard
                            firstMustCardResult =
                                strategySearchResultModel.mapToMustCreditCardSpendModel(
                                    estimateSpend,
                                    null,
                                    strategySearchResultModel.maximumSpendForCashback
                                )
                            mustCreditCards.add(firstMustCardResult)

                            // Check FirstMustCreditCard Can Earn Cashback ?
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

                                Timber.d("!==! UC5-FullBill firstBalanceCardResult-name${firstBalanceCardResult.creditCardName}")
                                Timber.d("!==! UC5-FullBill firstBalanceCardResult-balanceSpending: ${balanceSpending}")
                                Timber.d("!==! UC5-FullBill firstBalanceCardResult-cashbackEarned: ${firstBalanceCardResult.cashbackEarned}")
                            } else {
                                balanceSpending = balanceSpendOfMonth
                            }
                        }

                        else -> {
                            if (strategySearchResultModel.accumulateCashback == strategySearchResultModel.limitCashbackPerMonth) {
                                return@forEachIndexed
                            }
                            if (mustCreditCards.isEmpty()) {
                                firstMustCardResult =
                                    strategySearchResultModel.mapToMustCreditCardSpendModel(
                                        estimateSpend,
                                        null,
                                        strategySearchResultModel.maximumSpendForCashback
                                    )
                                mustCreditCards.add(firstMustCardResult)
                                // Check FirstMustCreditCard Can Earn Cashback ?
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

                                    //calculate BalanceSpending For Next Card
                                    balanceSpending =
                                        balanceSpendOfMonth - balanceSpendForMaximumCashback

                                    Timber.d("!==! UC5-FullBill firstBalanceCardResult-name${firstBalanceCardResult.creditCardName}")
                                    Timber.d("!==! UC5-FullBill firstBalanceCardResult-balanceSpending: ${balanceSpending}")
                                    Timber.d("!==! UC5-FullBill firstBalanceCardResult-cashbackEarned: ${firstBalanceCardResult.cashbackEarned}")
                                } else {
                                    balanceSpending = balanceSpendOfMonth
                                }
                            } else {
                                nextBalanceCardResult =
                                    if (balanceSpending >= strategySearchResultModel.maximumSpendForCashback) {
                                        strategySearchResultModel.mapToBalanceCreditCardSpendModel(
                                            strategySearchResultModel.maximumSpendForCashback,
                                            null,
                                            strategySearchResultModel.maximumSpendForCashback
                                        )
                                    } else {
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
                                Timber.d("!==! UC5-FullBill nextBalanceCardResult-name${nextBalanceCardResult.creditCardName}")
                                Timber.d("!==! UC5-FullBill nextBalanceCardResult-balanceSpending: ${balanceSpending}")
                                Timber.d("!==! UC5-FullBill nextBalanceCardResult-cashbackEarned: ${nextBalanceCardResult.cashbackEarned}")
                            }
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

            Timber.d("!==! UC5-FullBill mustCreditCards: ${mustCreditCards}")
            Timber.d("!==! UC5-FullBill balanceCreditCards: ${balanceCreditCards}")
            Timber.d("!==! UC5-FullBill TotalCashback: ${totalCashback}")

            //map model to show
            fullBillModel = StrategyCreditCardModel.FullBillModel(
                estimateSpend = estimateSpend.toString(),
                mustCreditCardSpends = mustCreditCards,
                balanceCreditCardSpends = balanceCreditCards,
                totalCashback = totalCashback,
                myExpenseLastMonth = myExpenseLastMonth.toString(),
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

    fun savedToCardBenefit(cardBenefitModel: CreditCardSearchResultModel) {
        val accumulateCashback =
            cardBenefitModel.accumulateCashback + cardBenefitModel.cashbackEarnedBath.toDouble()
                .toInt()
        Timber.d("!==! UpdateCardBenefit-accumulateCashback: ${accumulateCashback}")
        Timber.d("!==! UpdateCardBenefit-cardBenefitModel.id: ${cardBenefitModel.id}")
        val verifyAccumulateCashback =
            if (accumulateCashback > cardBenefitModel.limitCashbackPerMonth) {
                cardBenefitModel.limitCashbackPerMonth
            } else {
                accumulateCashback
            }
        myCardsCollectionStore.document(cardBenefitModel.id)
            .update(ACCUMULATE_CASHBACK, verifyAccumulateCashback)
            .addOnSuccessListener {
                _savedToMyCard.postValue(true)
                Timber.d("Update CardBenefit DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Timber.e("Update CardBenefit Error writing document", e)
            }
    }
}
