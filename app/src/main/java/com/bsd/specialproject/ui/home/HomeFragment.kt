package com.bsd.specialproject.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.FragmentHomeBinding
import com.bsd.specialproject.ui.addcreditcard.CreditCardViewModel
import com.bsd.specialproject.ui.home.adapter.MyCardsAdapter
import com.bsd.specialproject.utils.*
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val appRouter: AppRouter by inject()
    private val appPreference: AppPreference by inject()
    private val viewModel: HomeViewModel by viewModel()
    private val creditCardViewModel: CreditCardViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val myCardsAdapter by lazy {
        MyCardsAdapter { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        creditCardViewModel.fetchMyCards()
        viewModel.fetchMyExpense()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupMyExpenseLastMonth()
        binding.tvAddCreditCard.setOnClickListener {
            activity?.let {
                appRouter.toAddCreditCard(it)
            }
        }
        creditCardViewModel.myCardList.observe(viewLifecycleOwner) { myCreditCards ->
            myCardsAdapter.submitList(myCreditCards)
        }
        viewModel.myExpenseLastMonth.observe(viewLifecycleOwner) { myExpense ->
            if (myExpense.isNotEmpty()) {
                with(binding.viewMyExpense) {
                    textInputLayout.isVisible = false
                    tvMyExpense.apply {
                        text = this@HomeFragment.getString(
                            R.string.my_expense,
                            myExpense.toInt().toCurrencyFormat()
                        )
                        isVisible = true
                    }
                    ivEdit.isVisible = true
                }
            } else {
                with(binding.viewMyExpense) {
                    textInputLayout.isVisible = true
                    tvMyExpense.isVisible = false
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvMyCards) {
            val horizontalLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = horizontalLayoutManager
            adapter = myCardsAdapter
        }
    }

    private fun setupMyExpenseLastMonth() {
        var myExpenseText = appPreference.myExpenseLastMonth
        with(binding.viewMyExpense) {
            textInputLayout.setEndIconOnClickListener {
                if (myExpenseText.isNotEmpty()) {
                    viewModel.saveMyExpense(myExpenseText)
                    textInputLayout.apply {
                        isVisible = false
                        hideKeyboard()
                    }
                    ivEdit.isVisible = true
                }
            }
            textInputEditText.doAfterTextChanged {
                myExpenseText = it.toString()
            }
            ivEdit.setOnClickListener {
                tvMyExpense.isVisible = false
                ivEdit.isVisible = false
                textInputLayout.apply {
                    isVisible = true
                    showKeyboard()
                }
                textInputEditText.apply {
                    text = myExpenseText.toEditable()
                    setSelection(this.editableText.length)
                    requestFocus()
                }
            }
        }
    }
}
