package com.bsd.specialproject.ui.promotion

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bsd.specialproject.databinding.FragmentPromotionBinding

class PromotionFragment : Fragment() {

    private var _binding: FragmentPromotionBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // cal api
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPromotionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
