package com.bsd.specialproject.ui.searchresult

import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ActivitySearchResultBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.common.adapter.HorizontalWrapperAdapter
import com.bsd.specialproject.ui.home.model.ViewTitleModel
import com.bsd.specialproject.ui.searchresult.adapter.*
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
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
    private val searchResultViewModel: SearchResultViewModel by viewModel()

    private var _binding: ActivitySearchResultBinding? = null
    private val binding get() = _binding!!

    private val isLocationServiceEnable by lazy {
        SmartLocation.with(this).location().state().locationServicesEnabled()
    }
    private val smartLocation by lazy {
        SmartLocation.with(this).location()
    }
    private var myLocation: MyLocation? = null
    private var _isSpitBill: Boolean = false

    // Adapter
    private val bestOfCreditCardAdapter by lazy {
        BestOfCreditCardAdapter {

        }
    }
    private val horizontalBestOfCreditCardAdapter by lazy {
        HorizontalWrapperAdapter(
            bestOfCreditCardAdapter
        )
    }
    private val titleStrategyHeaderAdapter by lazy {
        TitleStrategyHeaderAdapter { click ->
            if (click is PromotionClick.SpitBillClick) {
                searchResultViewModel.fetchStrategyCreditCard(true)
            } else {
                searchResultViewModel.fetchStrategyCreditCard(false)
            }
        }
    }
    private val strategyCreditCardAdapter by lazy {
        StrategyCreditCardAdapter {

        }
    }
    private val titleCreditCardBenefitAdapter by lazy {
        TitleWithViewAllAdapter(
            ViewTitleModel(
                title = "สิทธิประโยชน์เครดิตเงินคืนของบัตร",
                isShowViewAll = false
            ),
            onClick = {}
        )
    }
    private val creditCardBenefitEmptyAdapter by lazy {
        CreditCardBenefitEmptyAdapter()
    }
    private val creditCardBenefitAdapter by lazy {
        CreditCardBenefitAdapter(onClick = { click ->
            if (click is CreditCardClick.SavedCashbackEarnedToMyCardClick) {
                showConfirmSaveToCardBenefit(click.item)
            }
        })
    }
    private val horizontalCreditCardBenefitAdapter by lazy {
        HorizontalWrapperAdapter(
            creditCardBenefitAdapter
        )
    }
    private val titleMyPromotionAdapter by lazy {
        TitleWithViewAllAdapter(
            ViewTitleModel(
                title = "โปรโมชั่นของฉัน",
                isShowViewAll = false
            ),
            onClick = {}
        )
    }
    private val myPromotionAdapter by lazy {
        MyPromotionAdapter(
            onClick = {

            }
        )
    }
    private val horizontalMyPromotionAdapter by lazy {
        HorizontalWrapperAdapter(
            myPromotionAdapter
        )
    }
    private val myPromotionEmptyAdapter by lazy {
        MyPromotionEmptyAdapter()
    }
    private val titleForYouPromotionAdapter by lazy {
        TitleForYouHeaderAdapter(onClicked = { click ->
            if (click is PromotionClick.FilterByCashbackClick) {
                searchResultViewModel.fetchForYouPromotion(null)
            } else {
                searchResultViewModel.fetchForYouPromotion(myLocation)
            }
        })
    }
    private val forYouPromotionAdapter by lazy {
        ForYouPromotionAdapter(
            onClick = { click ->
                if (click is PromotionClick.SelectedClick) {
                    showConfirmCreditCardListDialog(click.item)
                }
            }
        )
    }
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()
        ConcatAdapter(
            config,
            horizontalBestOfCreditCardAdapter,
            titleCreditCardBenefitAdapter,
            horizontalCreditCardBenefitAdapter,
            creditCardBenefitEmptyAdapter,
            titleStrategyHeaderAdapter,
            strategyCreditCardAdapter,
            titleMyPromotionAdapter,
            horizontalMyPromotionAdapter,
            myPromotionEmptyAdapter,
            titleForYouPromotionAdapter,
            forYouPromotionAdapter
        )
    }
    private var _searchResultModel: SearchResultModel? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupRecyclerView()

        searchResultViewModel.setArgumentModel(
            intent.getParcelableExtra(SEARCH_RESULT_MODEL)
        )
        searchResultViewModel.searchResultModel.observe(this) {
            titleStrategyHeaderAdapter.apply {
                isSpitBill = false
                submitList(
                    listOf(
                        ViewTitleModel(
                            title = "กลยุทย์การใช้จ่ายบัตรเครดิต",
                            isShowViewAll = false
                        )
                    )
                )
            }
            titleForYouPromotionAdapter.apply {
                isGrantedLocation = it.isGrantedLocation
                submitList(
                    listOf(
                        ViewTitleModel(
                            title = "โปรโมชั่นสำหรับคุณ",
                            isShowViewAll = false
                        )
                    )
                )
            }
            lifecycleScope.launch {
                searchResultViewModel.fetchMyPromotion()
                delay(1000)
                searchResultViewModel.fetchCardBenefitResult()
                delay(2000)
                searchResultViewModel.fetchStrategyCreditCard(_isSpitBill)
                if (!it.isGrantedLocation) {
                    searchResultViewModel.fetchForYouPromotion(null)
                } else {
                    searchResultViewModel.fetchForYouPromotion(myLocation)
                }
            }
        }
        searchResultViewModel.creditCardSearchResultList.observe(this) { creditCardResultModelList ->
            if (creditCardResultModelList.isNotEmpty()) {
                concatAdapter.removeAdapter(creditCardBenefitEmptyAdapter)
                creditCardBenefitAdapter.submitList(creditCardResultModelList)
            }
        }
        searchResultViewModel.foryouPromotionList.observe(this) { forYouPromotionList ->
            forYouPromotionAdapter.submitList(forYouPromotionList)
        }
        searchResultViewModel.savedToMyPromotion.observe(this) { isSuccessful ->
            if (isSuccessful) {
                this@SearchResultActivity.finish()
            }
        }
        searchResultViewModel.myPromotionList.observe(this) { myPromotionList ->
            if (myPromotionList.isNotEmpty()) {
                concatAdapter.removeAdapter(myPromotionEmptyAdapter)
                myPromotionAdapter.submitList(myPromotionList)
            }
        }
        searchResultViewModel.theBestOfCreditCard.observe(this) { creditCardId ->
            lifecycleScope.launch {
                delay(2000)
                var scrollToIndex = 0
                val currentList =
                    searchResultViewModel.creditCardSearchResultList.value?.mapIndexed { index, creditCardSearchResultModel ->
                        if (creditCardSearchResultModel.id == creditCardId) {
                            scrollToIndex = index
                            creditCardSearchResultModel.isCashbackHighest = true
                        }
                        creditCardSearchResultModel
                    }
                horizontalBestOfCreditCardAdapter.isScrollToPosition = scrollToIndex
                bestOfCreditCardAdapter.submitList(currentList)
            }
        }
        searchResultViewModel.strategyCreditCard.observe(this) {
            strategyCreditCardAdapter.submitList(it)
        }
        searchResultViewModel.savedToMyCard.observe(this) {
            if (it) {
                searchResultViewModel.fetchCardBenefitResult()
            }
        }
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
        val currentLocation = Location("50 Years Faculty of Commerce and Accountancy Tower")
        currentLocation.latitude = 13.733982474577605
        currentLocation.longitude = 100.52948211475976
        myLocation = MyLocation(currentLocation.latitude, currentLocation.longitude)

        Timber.d("!==! UC2.1 ForYouPromotion GetCurrentLocation-latitude: ${latitude}")
        Timber.d("!==! UC2.1 ForYouPromotion GetCurrentLocation-longitude: ${longitude}")
    }

    private fun showConfirmCreditCardListDialog(forYouPromotionModel: ForYouPromotionModel) {
        val initChecked = 0

        val creditCardResult =
            searchResultViewModel.creditCardSearchResultList.value?.filter { creditCardSearchResultModel ->
                forYouPromotionModel.creditCardRelation.any {
                    creditCardSearchResultModel.id == it
                }
            }
        val creditCardListName =
            creditCardResult?.map { creditCardSearchResultModel ->
                creditCardSearchResultModel.name
            }
        val creditCardListIds =
            creditCardResult?.map { creditCardSearchResultModel ->
                creditCardSearchResultModel.id
            }

        var cardSelectedId = ""
        var cardSelectedName = ""

        MaterialAlertDialogBuilder(this@SearchResultActivity)
            .setTitle(resources.getString(R.string.confirm_save_promotion_desc))
            .setSingleChoiceItems(
                creditCardListName?.toTypedArray(),
                initChecked
            ) { dialog, which ->
                // Respond to item chosen
                cardSelectedId = creditCardListIds?.get(which).toString()
                cardSelectedName = creditCardListName?.get(which).toString()

                Timber.d("!==! cardSelectedId: ${cardSelectedId}")
                Timber.d("!==! cardSelectedName: ${cardSelectedName}")
            }
            .setNeutralButton(resources.getString(R.string.common_decline)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.common_accept)) { dialog, which ->
                searchResultViewModel.savedToMyPromotion(
                    forYouPromotionModel,
                    cardSelectedId.ifEmpty {
                        creditCardListIds?.get(0).toString()
                    },
                    cardSelectedName.ifEmpty {
                        creditCardListName?.get(0).toString()
                    })
            }
            // Single-choice items (initialized with checked item)
            .show()
    }

    private fun showConfirmSaveToCardBenefit(creditCardSearchResultModel: CreditCardSearchResultModel) {
        MaterialAlertDialogBuilder(this@SearchResultActivity)
            .setTitle(resources.getString(R.string.confirm_save_card_benefit_title))
            .setMessage(resources.getString(R.string.confirm_save_card_benefit_desc))
            .setNegativeButton(resources.getString(R.string.common_decline)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.common_accept)) { _, _ ->
                searchResultViewModel.savedToCardBenefit(creditCardSearchResultModel)
            }
            .show()
    }
}
