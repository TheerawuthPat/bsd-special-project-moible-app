package com.bsd.specialproject.ui.addcreditcard.model

import com.google.gson.annotations.SerializedName

data class CreditCardModel(
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
    var isChecked: Boolean = false
)
