package com.bsd.specialproject.ui.searchresult.model

import com.bsd.specialproject.ui.common.model.CashbackCondition
import com.bsd.specialproject.utils.*

sealed class StrategyCreditCardModel {
    class SplitBillModel(
        val estimateSpend: String,
        val mustCreditCardSpends: List<MustCreditCardSpendModel>,
        val balanceCreditCardSpends: List<BalanceCreditCardSpendModel>,
        val totalCashback: Int,
        val myExpenseLastMonth: String,
        val balanceSpend: String
    ) : StrategyCreditCardModel()

    class FullBillModel(
        val estimateSpend: String,
        val mustCreditCardSpends: List<MustCreditCardSpendModel>,
        val balanceCreditCardSpends: List<BalanceCreditCardSpendModel>,
        val totalCashback: Int,
        val myExpenseLastMonth: String,
        val balanceSpend: String
    ) : StrategyCreditCardModel()
}

data class MustCreditCardSpendModel(
    val creditCardName: String,
    val balanceSpendOfMonth: Int,
    val cashbackEarned: Int,
    val balanceSpendForMaximumCashback: Int
)

data class BalanceCreditCardSpendModel(
    val creditCardName: String,
    val balanceSpendOfMonth: Int,
    val cashbackEarned: Int,
    val balanceSpendForMaximumCashback: Int
)

data class StrategySearchResultModel(
    val id: String,
    val name: String,
    val image: String,
    val earnedCategory: String,
    val cashbackPercent: Int,
    val cashbackConditions: List<CashbackCondition>,
    var cashbackEarnedBathPerMonth: Double,
    val mustToSpend: Int,
    val limitCashbackPerMonth: Int,
    var isCashbackHighest: Boolean = false,
    var indexOfCashbackHighest: Int = 0,
    val maximumSpendForCashback: Int
)

fun CreditCardSearchResultModel.mapToStrategySearchResultModel(mustToSpend: Int) =
    StrategySearchResultModel(
        id = this.id.toDefaultValue(),
        name = this.name.toDefaultValue(),
        image = this.imageUrl.toDefaultValue(),
        earnedCategory = earnedCategory,
        cashbackPercent = this.cashbackConditions.getCashbackPerTime(mustToSpend)
            .toDefaultValue(),
        cashbackEarnedBathPerMonth = calculatePercentageToBath(
            mustToSpend.toDouble(),
            this.cashbackConditions.getCashbackPerTime(mustToSpend).toDefaultValue()
        ),
        mustToSpend = mustToSpend,
        limitCashbackPerMonth = this.limitCashbackPerMonth.toDefaultValue(),
        maximumSpendForCashback = this.limitCashbackPerMonth.toDefaultValue()
            .calculateSpendingForCashback(
                this.cashbackConditions.getCashbackPerTime(mustToSpend).toDefaultValue()
            ),
        cashbackConditions = this.cashbackConditions.toDefaultValue()
    )

fun StrategySearchResultModel.mapToMustCreditCardSpendModel(
    balanceSpendOfMonth: Int,
    balanceCashEarned: Int?,
    balanceSpendForMaximumCashback: Int?
) = MustCreditCardSpendModel(
    creditCardName = this.name,
    balanceSpendOfMonth = balanceSpendOfMonth,
    cashbackEarned = calculateBalanceCashbackEarned(
        this.cashbackConditions,
        this.limitCashbackPerMonth,
        balanceSpendOfMonth,
        balanceCashEarned
    ),
    balanceSpendForMaximumCashback = balanceSpendForMaximumCashback ?: 1
)

fun StrategySearchResultModel.mapToBalanceCreditCardSpendModel(
    balanceSpendOfMonth: Int,
    balanceCashEarned: Int?,
    balanceSpendForMaximumCashback: Int
) =
    BalanceCreditCardSpendModel(
        creditCardName = this.name,
        balanceSpendOfMonth = balanceSpendOfMonth,
        cashbackEarned = calculateBalanceCashbackEarned(
            this.cashbackConditions,
            this.limitCashbackPerMonth,
            balanceSpendOfMonth,
            balanceCashEarned
        ),
        balanceSpendForMaximumCashback = balanceSpendForMaximumCashback
    )

fun calculateBalanceCashbackEarned(
    cashbackConditions: List<CashbackCondition>,
    limitCashbackPerMonth: Int,
    balanceSpendOfMonth: Int,
    balanceCashEarned: Int?
): Int {
    return if (balanceCashEarned != null) {
        calculateCashbackEarned(
            calculatePercentageToBath(
                balanceSpendOfMonth.toDouble(),
                cashbackConditions.getCashbackPerTime(balanceSpendOfMonth).toDefaultValue()
            ), limitCashbackPerMonth.toDouble()
        ).toInt() - balanceCashEarned
    } else {
        calculateCashbackEarned(
            calculatePercentageToBath(
                balanceSpendOfMonth.toDouble(),
                cashbackConditions.getCashbackPerTime(balanceSpendOfMonth).toDefaultValue()
            ), limitCashbackPerMonth.toDouble()
        ).toInt()
    }
}
