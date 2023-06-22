package com.bsd.specialproject.ui.searchresult.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.bsd.specialproject.constants.DATE_FORMAT
import com.bsd.specialproject.ui.common.model.CashbackCondition
import com.bsd.specialproject.utils.*
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

data class MyPromotionModel(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("promotionName")
    val promotionName: String? = null,
    @SerializedName("accumulateSpend")
    val accumulateSpend: Int? = null,
    @SerializedName("moreAccumulateSpend")
    var moreAccumulateSpend: Int? = null,
    @SerializedName("cashbackEarnedBath")
    val cashbackEarnedBath: Double? = null,
    @SerializedName("moreCashbackPercent")
    var moreCashbackPercent: Int? = null,
    @SerializedName("moreCashbackEarned")
    var moreCashbackEarned: Int? = null,
    @SerializedName("cashbackConditions")
    val cashbackConditions: List<CashbackCondition>? = null,
    @SerializedName("limitCashbackPerMonth")
    val limitCashbackPerMonth: Int? = null,
    @SerializedName("remainingDate")
    val remainingDate: String? = null,
    @SerializedName("savedDate")
    val savedDate: String? = null,
    @SerializedName("creditCardRelation")
    val creditCardRelation: List<String>? = null,
    @SerializedName("categoryType")
    val categoryType: List<String>? = null,
    @SerializedName("endDate")
    val endDate: String? = null,
    @SerializedName("startDate")
    val startDate: String? = null,
    @SerializedName("criteria")
    var criteria: Int? = 0,
    @SerializedName("cardSelectedName")
    val cardSelectedName: String? = null,
    @SerializedName("cardSelectedId")
    val cardSelectedId: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
fun ForYouPromotionModel.mapToMyPromotion(
    estimateSpend: Int,
    cardSelectedId: String,
    cardSelectedName: String
) = MyPromotionModel(
    id = this.id.toDefaultValue(),
    promotionName = this.name.toDefaultValue(),
    accumulateSpend = estimateSpend,
    moreAccumulateSpend = this.cashbackConditions.calculateMoreAccumulateSpend(
        estimateSpend,
        this.limitCashbackPerMonth
    ),
    cashbackEarnedBath = calculatePercentageToBath(
        estimateSpend.toDouble(),
        this.cashbackConditions.getCashbackPerTime(estimateSpend).toDefaultValue()
    ),
    moreCashbackPercent = 0,
    moreCashbackEarned = 0,
    cashbackConditions = this.cashbackConditions,
    limitCashbackPerMonth = this.limitCashbackPerMonth,
    remainingDate = calculateRemainingDays(this.endDate).toString(),
    savedDate = getCurrentDay(),
    creditCardRelation = listOf(cardSelectedId),
    categoryType = this.categoryType,
    startDate = this.startDate,
    endDate = this.endDate,
    cardSelectedName = cardSelectedName,
    cardSelectedId = cardSelectedId
)

@RequiresApi(Build.VERSION_CODES.O)
fun calculateRemainingDays(endDate: String): Long {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    val parsedEndDate = LocalDate.parse(endDate, formatter)

    return ChronoUnit.DAYS.between(currentDate, parsedEndDate)
}

fun getCurrentDay(): String {
    return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(Date())
}
