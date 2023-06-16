package com.bsd.specialproject.ui.searchresult.model

import com.bsd.specialproject.ui.addcreditcard.model.CashbackCondition
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.utils.toDefaultValue

data class CreditCardSearchResultModel(
    val name: String,
    val image: String,
    val earnedCategory: String,
    val cashbackPercent: String,
    val cashbackEarnedBath: String,
    val estimateSpending: String,
    var isCashbackHighest: Boolean = false,
    var indexOfCashbackHighest: Int = 0,
)

fun CreditCardModel.mapToCreditCardSearchResultModel(
    estimateSpending: Int,
    earnedCategory: String
) = CreditCardSearchResultModel(
    name = this.name.toDefaultValue(),
    image = this.imageUrl.toDefaultValue(),
    earnedCategory = earnedCategory,
    cashbackPercent = this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
        .toString(),
    cashbackEarnedBath = calculatePercentageToBath(
        estimateSpending.toDouble(),
        this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
    ).toString(),
    estimateSpending = estimateSpending.toString()
)

fun List<CashbackCondition>.getCashbackPerTime(estimateSpending: Int): Int {
    this.forEach { cbCondition ->
        if (estimateSpending in (cbCondition.minSpend.toDefaultValue()) until cbCondition.maxSpend.toDefaultValue()) {
            return cbCondition.cashbackPerTime.toDefaultValue()
        }
    }
    return 0
}

fun calculatePercentageToBath(estimateSpending: Double, percent: Int): Double {
    val percentage = percent.toDouble() / 100
    return estimateSpending * percentage
}

