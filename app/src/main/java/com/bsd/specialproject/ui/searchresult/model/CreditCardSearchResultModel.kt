package com.bsd.specialproject.ui.searchresult.model

import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.ui.common.model.CashbackCondition
import com.bsd.specialproject.utils.*

data class CreditCardSearchResultModel(
    val id: String,
    val name: String,
    val image: String,
    val earnedCategory: String,
    val cashbackPercent: String,
    val cashbackConditions: List<CashbackCondition>,
    val cashbackEarnedBath: String,
    val estimateSpending: String,
    val limitCashbackPerMonth: Int,
    var isCashbackHighest: Boolean = false,
    var indexOfCashbackHighest: Int = 0,
    val maximumSpendForCashback: Int
)

fun CreditCardResponse.mapToCreditCardSearchResultModel(
    estimateSpending: Int,
    earnedCategory: String
) = CreditCardSearchResultModel(
    id = this.id.toDefaultValue(),
    name = this.name.toDefaultValue(),
    image = this.imageUrl.toDefaultValue(),
    earnedCategory = earnedCategory,
    cashbackPercent = this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
        .toString(),
    cashbackEarnedBath = calculateCashbackEarned(
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
    cashbackConditions = this.cashbackConditions.toDefaultValue()
)

fun List<CreditCardSearchResultModel>.flagCashbackHighest() {
    val highestBath = this.maxByOrNull { it.cashbackEarnedBath }?.cashbackEarnedBath

    this.forEachIndexed { index, creditCardSearchResultModel ->
        if (creditCardSearchResultModel.cashbackEarnedBath == highestBath) {
            creditCardSearchResultModel.indexOfCashbackHighest = index
        }
    }
}
