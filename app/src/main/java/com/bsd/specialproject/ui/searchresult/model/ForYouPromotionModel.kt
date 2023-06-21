package com.bsd.specialproject.ui.searchresult.model

import com.bsd.specialproject.utils.*

data class ForYouPromotionModel(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val limitCashbackPerMonth: Int,
    val distance: Float?,
    val cashbackPercent: String,
    val cashbackEarnedBath: String
)

fun PromotionListResponse.mapToForYouPromotionModel(
    estimateSpending: Int,
    nearByDistance: Float?
) = ForYouPromotionModel(
    id = this.id.toDefaultValue(),
    name = this.name.toDefaultValue(),
    startDate = this.startDate.toDefaultValue(),
    endDate = this.endDate.toDefaultValue(),
    limitCashbackPerMonth = this.limitCashbackPerMonth.toDefaultValue(),
    distance = nearByDistance?.convertToTwoDecimalPlaces(),
    cashbackPercent = this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
        .toString(),
    cashbackEarnedBath = calculatePercentageToBath(
        estimateSpending.toDouble(),
        this.cashbackConditions?.getCashbackPerTime(estimateSpending).toDefaultValue()
    ).toString()
)
