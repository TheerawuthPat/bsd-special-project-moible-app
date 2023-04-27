package com.bsd.specialproject.ui.addcreditcard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsd.specialproject.databinding.ActivityAddCreditCardBinding
import com.bsd.specialproject.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCreditCardActivity : AppCompatActivity() {

    companion object {
        fun start(activity: AppCompatActivity) {
//            val intent = Intent(activity, AddCreditCardActivity::class.java)
            val intent = Intent(activity, AddCreditCardActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            activity.startActivity(intent)
        }
    }

    private val viewModel: AddCreditCardViewModel by viewModel()

    private var _binding: ActivityAddCreditCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddCreditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFirestore.setOnClickListener {
            viewModel.fetchCreditCard()
        }
    }
}
