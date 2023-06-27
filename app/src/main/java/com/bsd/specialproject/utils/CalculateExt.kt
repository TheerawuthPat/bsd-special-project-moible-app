package com.bsd.specialproject.utils

import com.bsd.specialproject.constants.UNLIMIT_MAX_SPEND
import com.bsd.specialproject.ui.common.model.CashbackCondition
import timber.log.Timber
import java.text.DecimalFormat

fun List<CashbackCondition>.getCashbackPerTime(estimateSpending: Int): Int {
    this.forEach { cbCondition ->
        if (estimateSpending in (cbCondition.minSpend.toDefaultValue()) until cbCondition.maxSpend.toDefaultValue() + 1) {
            return cbCondition.cashbackPerTime.toDefaultValue()
        }
    }
    return 1
}

fun calculatePercentageToBath(estimateSpending: Double, percent: Int): Double {
    val percentage = percent.toDouble() / 100
    return (estimateSpending * percentage).convertToTwoDecimalPlaces()
}

fun Float.convertToTwoDecimalPlaces(): Float {
    val df = DecimalFormat("#.##")
    return df.format(this).toFloat()
}

fun Double.convertToTwoDecimalPlaces(): Double {
    val df = DecimalFormat("#.##")
    return df.format(this).toDouble()
}

fun List<CashbackCondition>.calculateMoreAccumulateSpend(
    accumulateSpend: Int,
    limitCashbackPerMonth: Int
): Int {
    if (this.size > 1) {
        this.forEach { cbCondition ->
            if (accumulateSpend in (cbCondition.minSpend.toDefaultValue()) until cbCondition.maxSpend.toDefaultValue()+1) {
                return if (cbCondition.maxSpend == UNLIMIT_MAX_SPEND) {
                    val currentCashbackBath = calculatePercentageToBath(
                        accumulateSpend.toDouble(),
                        cbCondition.cashbackPerTime.toDefaultValue()
                    )
                    limitCashbackPerMonth - currentCashbackBath.toInt()
                } else {
                    (cbCondition.maxSpend.toDefaultValue() + 1) - accumulateSpend
                }
            }
        }
        return 0
    } else {
        return 0
    }
}

fun Int.calculateSpendingForCashback(cashbackPercent: Int): Int {
    return this.div(cashbackPercent) * 100
}

fun calculateTotalCashback(numbers: List<Int>): Int {
    var total = 0
    for (number in numbers) {
        total += number
    }
    return total
}

fun compareCashbackEarnedAndLimitCashback(
    cashbackEarnedBath: Double,
    limitCashbackPerMonth: Double
): Double {
    return if (cashbackEarnedBath > limitCashbackPerMonth) {
        limitCashbackPerMonth
    } else {
        cashbackEarnedBath
    }
}
