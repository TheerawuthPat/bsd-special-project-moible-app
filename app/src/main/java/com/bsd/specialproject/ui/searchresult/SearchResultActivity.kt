package com.bsd.specialproject.ui.searchresult

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.databinding.ActivitySearchResultBinding
import com.bsd.specialproject.ui.home.model.ViewTitleModel
import com.bsd.specialproject.ui.searchresult.adapter.CreditCardBenefitAdapter
import com.bsd.specialproject.ui.searchresult.adapter.TitleWithViewAllAdapter
import com.bsd.specialproject.ui.searchresult.model.SearchResultModel
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import org.koin.android.ext.android.inject
import timber.log.Timber

class SearchResultActivity : AppCompatActivity(), OnLocationUpdatedListener {

    companion object {
        private const val SEARCH_RESULT_MODEL = "SEARCH_RESULT_MODEL"

        fun start(activity: FragmentActivity, searchResultModel: SearchResultModel) {
            val intent = Intent(activity, SearchResultActivity::class.java).apply {
                putExtra(SEARCH_RESULT_MODEL, searchResultModel)
            }
            activity.startActivity(intent)
        }
    }

    private val appRouter: AppRouter by inject()

    private var _binding: ActivitySearchResultBinding? = null
    private val binding get() = _binding!!

    private val isLocationServiceEnable by lazy {
        SmartLocation.with(this).location().state().locationServicesEnabled()
    }
    private val smartLocation by lazy {
        SmartLocation.with(this).location()
    }

    // Adapter
    private val titleCreditCardBenefitAdapter by lazy {
        TitleWithViewAllAdapter(
            ViewTitleModel(
                title = "สิทธิประโยชน์เครดิตเงินคืนของบัตร",
                isShowViewAll = false
            ),
            onClick = {
                // click view all
            }
        )
    }
    private val creditCardBenefitAdapter by lazy {
        CreditCardBenefitAdapter(onClick = {

        })
    }
    private val titleMyPromotionAdapter by lazy {
        TitleWithViewAllAdapter(
            ViewTitleModel(
                title = "โปรโมชั่นของฉัน",
                isShowViewAll = false
            ),
            onClick = {
                // click view all
            }
        )
    }
    private val myPromotionAdapter by lazy {

    }
    val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()
        ConcatAdapter(config, titleCreditCardBenefitAdapter, creditCardBenefitAdapter, titlePromotionTrackingAdapter)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        with(binding.viewToolbar) {
            tvToolbarTitle.text = "ผลลัพธ์การแนะนำบัตรเครดิต"
            ivToolbarBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setupRecyclerView() = with(binding.recyclerView) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = concatAdapter
    }

    override fun onStart() {
        super.onStart()
        if (isLocationServiceEnable) {
            smartLocation.start(this)
        }
    }

    override fun onStop() {
        super.onStop()
        smartLocation.stop()
    }

    override fun onLocationUpdated(location: Location) {
        val latitude: Double = location.latitude
        val longitude: Double = location.longitude
        Timber.d("!==! lat: ${latitude}")
        Timber.d("!==! lng: ${longitude}")
    }
}
