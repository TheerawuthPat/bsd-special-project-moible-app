package com.bsd.specialproject.ui.home

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.FragmentHomeBinding
import com.bsd.specialproject.ui.addcreditcard.CreditCardViewModel
import com.bsd.specialproject.ui.home.adapter.MyCardsAdapter
import com.bsd.specialproject.ui.searchresult.model.SearchResultModel
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

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            requireContext().showToastMessage("Granted Location Permission")
        } else {
            requireContext().showToastMessage("Denied Location Permission")
        }
        navigateToSearchResultPage(isGranted)
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
        setupCategoryDropDownMenu()
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

    private fun setupCategoryDropDownMenu() {
        val categoryMenuName = resources.getStringArray(R.array.category_menu)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_category_dropdown, categoryMenuName)
        with(binding.viewFindingBestOfCard) {
            autocompleteCategoryMenu.apply {
                setAdapter(arrayAdapter)
                setOnItemClickListener { adapterView, view, i, l ->
                    btnFind.isEnabled = textInputEditTextFindCard.text.toString()
                        .isNotEmpty() && autocompleteCategoryMenu.text.toString().isNotEmpty()
                }
            }
            textInputEditTextFindCard.doAfterTextChanged {
                btnFind.isEnabled = it.toString().isNotEmpty() && autocompleteCategoryMenu.text.toString().isNotEmpty()
            }
            btnFind.setOnClickListener {
                handleRequestLocationPermission()
            }
        }
    }

    private fun handleRequestLocationPermission() {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun navigateToSearchResultPage(isGranted: Boolean) {
        val searchResultModel = SearchResultModel(
            estimateSpend = binding.viewFindingBestOfCard.textInputEditTextFindCard.text.toString().toInt(),
            categorySpend = binding.viewFindingBestOfCard.autocompleteCategoryMenu.text.toString(),
            isGrantedLocation = isGranted
        )
        appRouter.toSearchResult(requireActivity(), searchResultModel)
    }
}
