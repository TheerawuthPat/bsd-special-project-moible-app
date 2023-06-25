package com.bsd.specialproject.ui.managecreditcard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bsd.specialproject.databinding.FragmentManageCreditCardBinding
import com.bsd.specialproject.ui.addcreditcard.CreditCardViewModel
import com.bsd.specialproject.ui.addcreditcard.adapter.CreditCardAdapter
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.utils.toDefaultValue
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ManageCreditCardFragment : Fragment() {

    private val creditCardViewModel: CreditCardViewModel by viewModel()

    private var _binding: FragmentManageCreditCardBinding? = null
    private val binding get() = _binding!!

    private val creditCardAdapter by lazy {
        CreditCardAdapter(onClick = { clicked ->
            if (clicked is CreditCardClick.SelectedClick) {
                bindSavedCardsButton()
            }
        }, isEnableDelete = true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        creditCardViewModel.fetchMyCards()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageCreditCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        creditCardViewModel.myCardList.observe(viewLifecycleOwner) { myCreditCards ->
            creditCardAdapter.submitList(myCreditCards)
        }
    }

    private fun bindSavedCardsButton() {
        val isSelectedCard = creditCardAdapter.currentList.any { it.isChecked }
        val creditCardChecked = creditCardAdapter.currentList.filter {
            it.isChecked
        }.map {
            it.id.toDefaultValue()
        }
        Timber.d("!==! UC1-RemoveCard: creditCardChecked: ${creditCardChecked}")

        with(binding.btnSaved) {
            isEnabled = isSelectedCard.toDefaultValue()
            setOnClickListener {
                creditCardViewModel.removedMyCards(creditCardChecked.toDefaultValue())
                creditCardViewModel.fetchMyCards()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = creditCardAdapter
    }
}
