package com.bsd.specialproject.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ActivityMainBinding
import com.bsd.specialproject.ui.home.HomeFragment
import com.bsd.specialproject.ui.promotion.PromotionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            activity.startActivity(intent)
        }
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        lateinit var selectedFragment: Fragment
        when (item.itemId) {
            R.id.homeFragment -> {
                selectedFragment = HomeFragment()
            }
            R.id.promotionFragment -> {
                selectedFragment = PromotionFragment()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.navHostFragment, selectedFragment).commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
    }
}
