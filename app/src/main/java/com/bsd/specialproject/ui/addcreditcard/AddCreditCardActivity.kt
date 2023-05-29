package com.bsd.specialproject.ui.addcreditcard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.databinding.ActivityAddCreditCardBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.CreditCardAdapter
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.utils.toDefaultValue
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddCreditCardActivity : AppCompatActivity() {

    companion object {
        fun start(activity: FragmentActivity) {
            val intent = Intent(activity, AddCreditCardActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private val appRouter: AppRouter by inject()
    private val creditCardViewModel: CreditCardViewModel by viewModel()

    private var _binding: ActivityAddCreditCardBinding? = null
    private val binding get() = _binding!!

    private val creditCardAdapter by lazy {
        CreditCardAdapter(onClick = { clicked ->
            if (clicked is CreditCardClick.SelectedClick) {
                bindSavedCardsButton()
            }
        }, isEnableDelete = false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddCreditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupRecyclerView()

        creditCardViewModel.fetchAddMoreCreditCard()
        creditCardViewModel.creditCardList.observe(this) { creditCardList ->
            creditCardAdapter.submitList(creditCardList)
        }
    }

    private fun setupToolbar() {
        with(binding.viewToolbar) {
            tvToolbarTitle.text = "เพิ่มบัตรเครดิต"
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
                creditCardViewModel.addedToMyCards(creditCardChecked)
                appRouter.toMain(this@AddCreditCardActivity)
            }
        }
    }
}
