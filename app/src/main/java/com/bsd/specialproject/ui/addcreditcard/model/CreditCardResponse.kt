package com.bsd.specialproject.ui.addcreditcard.model

import com.bsd.specialproject.ui.common.model.CashbackCondition
import com.google.gson.annotations.SerializedName

data class CreditCardResponse(
    @SerializedName("cashbackConditions")
    val cashbackConditions: List<CashbackCondition>? = null,
    @SerializedName("cashbackType")
    val cashbackType: String? = null,
    @SerializedName("categoryType")
    val categoryType: List<String>? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("limitCashbackPerMonth")
    val limitCashbackPerMonth: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("sourceBank")
    val sourceBank: String? = null,
    @SerializedName("isChecked")
    var isChecked: Boolean = false,
    @SerializedName("accumulateCashback")
    var accumulateCashback: Int? = null,
    @SerializedName("accumulateCashback")
    var accumulateSpend: Int? = null,
)
