package com.bsd.specialproject.ui.promotion

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bsd.specialproject.databinding.FragmentPromotionBinding
import com.bsd.specialproject.ui.searchresult.SearchResultViewModel
import com.bsd.specialproject.ui.searchresult.adapter.MyPromotionAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPromotionFragment : Fragment() {

    private val myPromotionViewModel: MyPromotionViewModel by viewModel()

    private var _binding: FragmentPromotionBinding? = null
    private val binding get() = _binding!!

    private val myPromotionAdapter by lazy {
        MyPromotionAdapter(
            onClick = {

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromotionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()

        myPromotionViewModel.fetchMyPromotion()
        myPromotionViewModel.myPromotionList.observe(viewLifecycleOwner) { myPromotionList ->
            myPromotionAdapter.submitList(myPromotionList)
        }
    }

    private fun setupToolbar() {
        with(binding.viewToolbar) {
            ivToolbarBack.isVisible = false
            tvToolbarTitle.text = "โปรโมชั่นของฉัน"
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = myPromotionAdapter
    }
}
