package com.bsd.specialproject.ui.addcreditcard.adapter.click

import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse

sealed class CreditCardClick {
    class SelectedClick(
        val item: CreditCardResponse
    ): CreditCardClick()
}
