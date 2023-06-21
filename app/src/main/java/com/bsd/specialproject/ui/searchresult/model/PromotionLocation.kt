package com.bsd.specialproject.ui.searchresult.model

import com.google.gson.annotations.SerializedName

data class PromotionLocation(
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lng")
    val lng: Double? = null,
    @SerializedName("placeName")
    val placeName: String? = null
)
