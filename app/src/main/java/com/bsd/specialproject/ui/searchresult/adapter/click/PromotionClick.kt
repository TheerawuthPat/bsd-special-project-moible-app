package com.bsd.specialproject.ui.searchresult.adapter.click

import com.bsd.specialproject.ui.searchresult.model.ForYouPromotionModel

sealed class PromotionClick {
    class SelectedClick(
        val item: ForYouPromotionModel
    ) : PromotionClick()

    object FilterByDistanceClick : PromotionClick()

    object FilterByCashbackClick : PromotionClick()
}
