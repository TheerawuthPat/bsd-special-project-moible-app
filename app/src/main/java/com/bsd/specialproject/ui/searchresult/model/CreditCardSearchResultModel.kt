package com.bsd.specialproject.ui.searchresult.model

import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.ui.common.model.CashbackCondition
import com.bsd.specialproject.utils.*

data class CreditCardSearchResultModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val earnedCategory: String,
    val cashbackPercent: String,
    val cashbackConditions: List<CashbackCondition>,
    val cashbackEarnedBath: String,
    val estimateSpending: String,
    val limitCashbackPerMonth: Int,
    var isCashbackHighest: Boolean = false,
    var indexOfCashbackHighest: Int = 0,
    val maximumSpendForCashback: Int,
    val accumulateCashback: Int,
    val cashbackType: String,
    val categoryType: List<String>,
    val sourceBank: String,
    var isChecked: Boolean = false,
    var accumulateSpend: Int? = null,
)

fun CreditCardResponse.mapToCreditCardSearchResultModel(
    estimateSpending: Int,
    earnedCategory: String
) = CreditCardSearchResultModel(
    id = this.id.toDefaultValue(),
    name = this.name.toDefaultValue(),
    imageUrl = this.imageUrl.toDefaultValue(),
    earnedCategory = earnedCategory,
    cashbackPercent = this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
        .toString(),
    cashbackEarnedBath = compareCashbackEarnedAndLimitCashback(
        calculatePercentageToBath(
            estimateSpending.toDouble(),
            this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
        ), this.limitCashbackPerMonth?.toDouble().toDefaultValue()
    ).toString(),
    estimateSpending = estimateSpending.toString(),
    limitCashbackPerMonth = this.limitCashbackPerMonth.toDefaultValue(),
    maximumSpendForCashback = this.limitCashbackPerMonth.toDefaultValue()
        .calculateSpendingForCashback(
            this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
        ),
    cashbackConditions = this.cashbackConditions.toDefaultValue(),
    accumulateCashback = this.accumulateCashback.toDefaultValue(),
    cashbackType = this.cashbackType.toDefaultValue(),
    categoryType = this.categoryType.toDefaultValue(),
    sourceBank = this.sourceBank.toDefaultValue()
)

fun List<CreditCardSearchResultModel>.flagCashbackHighest() {
    val highestBath = this.maxByOrNull { it.cashbackEarnedBath }?.cashbackEarnedBath

    this.forEachIndexed { index, creditCardSearchResultModel ->
        if (creditCardSearchResultModel.cashbackEarnedBath == highestBath) {
            creditCardSearchResultModel.indexOfCashbackHighest = index
        }
    }
}

fun CreditCardSearchResultModel.mapToCreditCardResponseForUpdateMyCard(accumulateCashback: Int) = CreditCardResponse(
    id = this.id.toDefaultValue(),
    name = this.name.toDefaultValue(),
    cashbackConditions = this.cashbackConditions.toDefaultValue(),
    limitCashbackPerMonth = this.limitCashbackPerMonth.toDefaultValue(),
    accumulateCashback = accumulateCashback,
    cashbackType = this.cashbackType,
    categoryType = this.categoryType,
    imageUrl = this.imageUrl,
    sourceBank = this.sourceBank
)
