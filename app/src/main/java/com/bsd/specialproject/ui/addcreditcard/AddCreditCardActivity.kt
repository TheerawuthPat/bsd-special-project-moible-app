package com.bsd.specialproject.ui.addcreditcard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsd.specialproject.databinding.ActivityAddCreditCardBinding

class AddCreditCardActivity : AppCompatActivity() {

    companion object {
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, AddCreditCardActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private var _binding: ActivityAddCreditCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddCreditCardBinding.inflate(layoutInflater)
    }
}
