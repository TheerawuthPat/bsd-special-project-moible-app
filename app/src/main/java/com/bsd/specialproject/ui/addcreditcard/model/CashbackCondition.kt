package com.bsd.specialproject.ui.addcreditcard.model

import com.google.gson.annotations.SerializedName

data class CashbackCondition(
    @SerializedName("cashbackPerTime")
    val cashbackPerTime: Int? = null,
    @SerializedName("maxSpend")
    val maxSpend: Int? = null,
    @SerializedName("minSpend")
    val minSpend: Int? = null
)
