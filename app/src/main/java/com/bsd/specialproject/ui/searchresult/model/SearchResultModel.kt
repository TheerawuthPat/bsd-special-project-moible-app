package com.bsd.specialproject.ui.searchresult.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResultModel(
    val estimateSpend: Int,
    val categorySpend: String,
    val isGrantedLocation: Boolean
) : Parcelable
