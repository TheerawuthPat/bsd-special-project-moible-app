package com.bsd.specialproject.ui.addcreditcard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.databinding.ActivityAddCreditCardBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.CreditCardAdapter
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.utils.toDefaultValue
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCreditCardActivity : AppCompatActivity() {

    private val appRouter: AppRouter by inject()

    companion object {
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, AddCreditCardActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            activity.startActivity(intent)
        }
    }

    private val viewModel: AddCreditCardViewModel by viewModel()

    private var _binding: ActivityAddCreditCardBinding? = null
    private val binding get() = _binding!!

    private val creditCardAdapter by lazy {
        CreditCardAdapter(onClick = { clicked ->
            if (clicked is CreditCardClick.SelectedClick) {
                bindSavedCardsButton()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddCreditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupRecyclerView()

        viewModel.fetchCreditCard()
        viewModel.creditCardList.observe(this) { creditCardList ->
            creditCardAdapter.submitList(creditCardList)
            bindSavedCardsButton()
        }
    }

    private fun setupToolbar() {
        with(binding.viewToolbar) {
            tvToolbarTitle.text = "Add Credit Cards"
            ivToolbarBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = creditCardAdapter
    }

    private fun bindSavedCardsButton() {
        val isSelectedCard = creditCardAdapter.currentList.any { it.isChecked }
        val creditCardChecked = creditCardAdapter.currentList.filter {
            it.isChecked
        }.map {
            it.id.toDefaultValue()
        }
        with(binding.btnSaved) {
            isEnabled = isSelectedCard
            setOnClickListener {
                viewModel.savedToMyCards(creditCardChecked)
                appRouter.toMain(this@AddCreditCardActivity)
            }
        }
    }
}
