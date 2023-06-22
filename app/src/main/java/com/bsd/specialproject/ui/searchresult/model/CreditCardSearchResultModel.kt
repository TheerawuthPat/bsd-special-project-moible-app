package com.bsd.specialproject.ui.searchresult.model

import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.utils.*

data class CreditCardSearchResultModel(
    val id: String,
    val name: String,
    val image: String,
    val earnedCategory: String,
    val cashbackPercent: String,
    val cashbackEarnedBath: String,
    val estimateSpending: String,
    var isCashbackHighest: Boolean = false,
    var indexOfCashbackHighest: Int = 0,
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
    cashbackEarnedBath = calculatePercentageToBath(
        estimateSpending.toDouble(),
        this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
    ).toString(),
    estimateSpending = estimateSpending.toString()
)

fun List<CreditCardSearchResultModel>.flagCashbackHighest() {
    val highestBath = this.maxByOrNull { it.cashbackEarnedBath }?.cashbackEarnedBath

    this.forEachIndexed { index, creditCardSearchResultModel ->
        if (creditCardSearchResultModel.cashbackEarnedBath == highestBath) {
//            creditCardSearchResultModel.isCashbackHighest = true
            creditCardSearchResultModel.indexOfCashbackHighest = index
        }
    }
}
