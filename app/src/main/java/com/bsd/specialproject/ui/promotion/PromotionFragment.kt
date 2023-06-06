package com.bsd.specialproject.ui.promotion

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bsd.specialproject.databinding.FragmentPromotionBinding
import com.bsd.specialproject.ui.home.HomeFragment
import com.bsd.specialproject.utils.checkAndRequestPermissions

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
        handleRequestLocationPermission()
    }

    private fun handleRequestLocationPermission() {
        requireActivity().checkAndRequestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            HomeFragment.LOCATION_PERMISSION_CODE
        ) {
            Toast.makeText(requireContext(), "Allow Location", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == HomeFragment.LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Accept Allow Location", Toast.LENGTH_LONG).show()
            }
        }
    }
}
