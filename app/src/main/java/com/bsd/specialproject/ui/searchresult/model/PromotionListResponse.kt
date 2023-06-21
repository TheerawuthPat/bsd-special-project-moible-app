package com.bsd.specialproject.ui.searchresult.model

import com.bsd.specialproject.ui.common.model.CashbackCondition
import com.google.gson.annotations.SerializedName

data class PromotionListResponse(
    @SerializedName("cashbackConditions")
    val cashbackConditions: List<CashbackCondition>? = null,
    @SerializedName("cashbackType")
    val cashbackType: String? = null,
    @SerializedName("categoryType")
    val categoryType: List<String>? = null,
    @SerializedName("creditCardRelation")
    val creditCardRelation: List<String>? = null,
    @SerializedName("endDate")
    val endDate: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("limitCashbackPerMonth")
    val limitCashbackPerMonth: Int? = null,
    @SerializedName("location")
    val location: List<PromotionLocation>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("sourceBank")
    val sourceBank: String? = null,
    @SerializedName("startDate")
    val startDate: String? = null
)
