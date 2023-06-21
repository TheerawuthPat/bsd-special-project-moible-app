package com.bsd.specialproject.ui.searchresult.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.bsd.specialproject.constants.DATE_FORMAT
import com.bsd.specialproject.ui.common.model.CashbackCondition
import com.bsd.specialproject.utils.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

data class MyPromotionModel(
    val id: String,
    val promotionName: String,
    val accumulateSpend: Int,
    val moreAccumulateSpend: Int,
    val cashbackEarnedBath: Double,
    val moreCashbackEarned: Int,
    val cashbackConditions: List<CashbackCondition>,
    val limitCashbackPerMonth: Int,
    val remainingDate: String,
    val savedDate: String,
)

@RequiresApi(Build.VERSION_CODES.O)
fun ForYouPromotionModel.mapToMyPromotion(
    estimateSpend: Int,
    moreCashbackEarned: Int
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
    moreCashbackEarned = moreCashbackEarned,
    cashbackConditions = this.cashbackConditions,
    limitCashbackPerMonth = this.limitCashbackPerMonth,
    remainingDate = calculateRemainingDays(this.endDate).toString(),
    savedDate = getCurrentDay()
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
