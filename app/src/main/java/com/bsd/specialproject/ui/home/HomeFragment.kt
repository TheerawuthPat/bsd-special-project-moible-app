package com.bsd.specialproject.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.databinding.FragmentHomeBinding
import com.bsd.specialproject.ui.addcreditcard.CreditCardViewModel
import com.bsd.specialproject.ui.home.adapter.MyCardsAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val appRouter: AppRouter by inject()
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        binding.tvAddCreditCard.setOnClickListener {
            activity?.let {
                appRouter.toAddCreditCard(it)
            }
        }
        creditCardViewModel.myCardList.observe(viewLifecycleOwner) { myCreditCards ->
            myCardsAdapter.submitList(myCreditCards)
        }
    }

    private fun initRecyclerView() {
        with(binding.rvMyCards) {
            val horizontalLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = horizontalLayoutManager
            adapter = myCardsAdapter
        }
    }
}
