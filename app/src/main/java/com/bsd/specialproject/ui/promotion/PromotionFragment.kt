package com.bsd.specialproject.ui.promotion

import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bsd.specialproject.databinding.FragmentPromotionBinding
import timber.log.Timber

class PromotionFragment : Fragment() {

    private var _binding: FragmentPromotionBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // cal api
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPromotionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testDistanceToMethod()
    }

    private fun testDistanceToMethod() {
        val currentLocation = Location("50 Years Faculty of Commerce and Accountancy Tower")
        currentLocation.latitude = 13.733982474577605
        currentLocation.longitude = 100.52948211475976

        val destinationPoint = Location("Faculty of Commerce and Accountancy (CU)")
        destinationPoint.latitude = 13.727837439035023
        destinationPoint.longitude = 100.5325308949121

        val distance: Float = currentLocation.distanceTo(destinationPoint)

        Timber.d("!==! Distance: ${distance} meters")
    }
}
