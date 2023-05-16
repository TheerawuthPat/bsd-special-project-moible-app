package com.bsd.specialproject.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.databinding.FragmentHomeBinding
import com.bsd.specialproject.ui.home.adapter.MyCardsAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val appRouter: AppRouter by inject()
    private val viewModel: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

//    private val concatAdapter by lazy {
//        val config = ConcatAdapter.Config.Builder().apply {
//            setIsolateViewTypes(false)
//        }.build()
//        ConcatAdapter(
//            config,
//            myCardsHeaderAdapter,
//            myCardsAdapter
//        )
//    }

//    private val myCardsHeaderAdapter by lazy {
//        MyCardsHeaderAdapter(
//            ViewTitleModel(
//                title = "My Cards",
//                icon = R.drawable.outline_add_card_24
//            ),
//            onClick = {
//                //handle open add credit card
//            }
//        )
//    }

    private val myCardsAdapter by lazy {
        MyCardsAdapter { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchMyCreditCard()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        viewModel.myCreditCardList.observe(viewLifecycleOwner) { myCreditCards ->
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
