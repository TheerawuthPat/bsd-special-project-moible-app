package com.bsd.specialproject.ui.splashscreen

import android.animation.Animator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.databinding.ActivitySplashScreenBinding
import com.bsd.specialproject.ui.addcreditcard.CreditCardViewModel
import com.bsd.specialproject.utils.sharedprefer.AppPreference
import com.bsd.specialproject.utils.toDefaultValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {

    private val appPreference: AppPreference by inject()
    private val appRouter: AppRouter by inject()
    private val creditCardViewModel: CreditCardViewModel by inject()

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        creditCardViewModel.fetchMyCardsFromFirebase()

        lifecycleScope.launch {
            delay(1000)
            binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    if (creditCardViewModel.myCardList.value?.isNotEmpty() == true) {
                        lifecycleScope.launch {
                            appPreference.myCreditCards =
                                creditCardViewModel.myCardList.value?.map {
                                    it.id.toDefaultValue()
                                }?.toSet()
                            appRouter.toMain(this@SplashScreenActivity)
                        }
                    } else {
                        appRouter.toAddCreditCard(this@SplashScreenActivity)
                    }
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
        }
    }
}
