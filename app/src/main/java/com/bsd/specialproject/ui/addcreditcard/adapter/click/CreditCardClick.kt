package com.bsd.specialproject.ui.addcreditcard.adapter.click

import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.ui.searchresult.model.CreditCardSearchResultModel

sealed class CreditCardClick {
    class SelectedClick(
        val item: CreditCardResponse
    ): CreditCardClick()

    class SavedCashbackEarnedToMyCardClick(
        val item: CreditCardSearchResultModel
    ): CreditCardClick()
}
