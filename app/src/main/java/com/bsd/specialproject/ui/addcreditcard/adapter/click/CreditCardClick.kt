package com.bsd.specialproject.ui.addcreditcard.adapter.click

import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel

sealed class CreditCardClick {
    class SelectedClick(
        val item: CreditCardModel
    ): CreditCardClick()
}
