package spiral.bit.dev.simpledecidehelper.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.*
import com.android.billingclient.api.BillingClient.SkuType.SUBS
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.ActivityMainBinding
import spiral.bit.dev.simpledecidehelper.fragments.CompletedTasksFragment
import spiral.bit.dev.simpledecidehelper.fragments.MyTasksFragment
import spiral.bit.dev.simpledecidehelper.listeners.ComplDismissListener
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.other.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), DismissListener,
    ComplDismissListener, PurchasesUpdatedListener {

    private val decisionsBinding: ActivityMainBinding by viewBinding()

    @Inject
    lateinit var defSharedPrefs: SharedPreferences
    private var isOpenedDrawer = false
    private val myTasksFragment by lazy { MyTasksFragment() }
    private val complTasksFragment by lazy { CompletedTasksFragment() }
    private val decisionBottomSheet by lazy { DecisionBottomSheet() }
    private val completeBottomSheet by lazy { CompleteBottomSheet() }
    private val billingClient by lazy {
        newBuilder(this).enablePendingPurchases()
            .setListener(this).build()
    }

    private val billingListener = object : BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingResponseCode.OK) {
                val queryPurchase = billingClient.queryPurchases(SUBS)
                val queryPurchases = queryPurchase.purchasesList
                if (queryPurchases != null && queryPurchases.size > 0) {
                    this@MainActivity.handlePurchases(
                        queryPurchases,
                        billingClient,
                        defSharedPrefs,
                        ackPurchaseListener
                    )
                } else defSharedPrefs saveSubscribeValueToPref false
            }
        }

        override fun onBillingServiceDisconnected() {
        }
    }
    private var ackPurchaseListener =
        AcknowledgePurchaseResponseListener { billingResult ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                defSharedPrefs saveSubscribeValueToPref true
                recreate()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            defSharedPrefs startWorkManager this@MainActivity
            billingClient.startConnection(billingListener)
            setUpViews()
        }
    }

    private fun setUpViews() {
        if (defSharedPrefs.getSubscribeValueFromPref()) {
            decisionsBinding.adView.isVisible = false
        } else {
            decisionsBinding.adView.isVisible = true
            decisionsBinding.adView.loadAd(AdManagerAdRequest.Builder().build())
        }

        myTasksFragment.setModalBottomSheet(decisionBottomSheet)

        setUpListeners()
        setUpDrawer()
    }

    private fun setUpListeners() {
        decisionBottomSheet.setMyDismissListener(this)
        completeBottomSheet.setMyDismissListener(this)

        decisionsBinding.purchaseProBtn.setOnClickListener {
            decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
        }

        decisionsBinding.fabAddTask.setOnClickListener {
            supportFragmentManager.findFragmentById(decisionsBinding.mainFragmentContainer.id)!!
                .apply {
                    if (this is MyTasksFragment)
                        decisionBottomSheet.show(supportFragmentManager, TAG_MODAL_BOTTOM_SHEET)
                    else if (this is CompletedTasksFragment)
                        completeBottomSheet.show(supportFragmentManager, TAG_MODAL_BOTTOM_SHEET)
                }
        }

        decisionsBinding.tabBar.onItemSelected = {
            when (it) {
                0 -> changeFragments(myTasksFragment, true, R.id.main_fragment_container)
                    .also { decisionsBinding.fabAddTask.setImageResource(R.drawable.ic_add_pencil) }
                1 -> changeFragments(complTasksFragment, true, R.id.main_fragment_container)
                    .also { decisionsBinding.fabAddTask.setImageResource(R.drawable.ic_delete) }
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun setUpDrawer() {
        decisionsBinding.hambMenu.setOnClickListener {
            isOpenedDrawer = if (isOpenedDrawer) {
                decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                false
            } else {
                decisionsBinding.drawerLayout.openDrawer(Gravity.LEFT)
                true
            }
        }

        decisionsBinding.navigationView.apply {
            requestFocus()
            bringToFront()
            setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.completed_decisions -> {
                        decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                        decisionsBinding.tabBar.itemActiveIndex = 1
                        changeFragments(complTasksFragment, true, R.id.main_fragment_container)
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.rate_app -> {
                        this@MainActivity rateApp decisionsBinding
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.invite_a_friend -> {
                        this@MainActivity inviteFriend decisionsBinding
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.night_theme -> {
                        it setUpNightMode decisionsBinding
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.buy_pro_version -> {
                        decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                        this@MainActivity subscribe billingClient
                        return@setNavigationItemSelectedListener true
                    }
                    else -> {
                        return@setNavigationItemSelectedListener false
                    }
                }
            }
            if (defSharedPrefs.getSubscribeValueFromPref())
                menu.findItem(R.id.buy_pro_version).isVisible = false
        }
    }

    override fun dismiss() {
        decisionBottomSheet.dismiss()
    }

    override fun editDismiss() {
        completeBottomSheet.dismiss()
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        purchases?.let {
            when (billingResult.responseCode) {
                BillingResponseCode.OK -> this.handlePurchases(
                    purchases,
                    billingClient,
                    defSharedPrefs,
                    ackPurchaseListener
                )
                BillingResponseCode.ITEM_ALREADY_OWNED ->
                    billingClient.queryPurchases(SUBS).also {
                        it.purchasesList
                            ?.let { purchases ->
                                this.handlePurchases(
                                    purchases,
                                    billingClient,
                                    defSharedPrefs,
                                    ackPurchaseListener
                                )
                            }
                    }
                BillingResponseCode.USER_CANCELED -> {
                    applicationContext showToast getString(R.string.sub_cancelled)
                }
                else -> {
                    applicationContext showToast "Ошибка - ${billingResult.debugMessage}"
                }
            }
        } ?: Log.d(LOG_TAG, "Purchases is null")
    }

    override fun onDestroy() {
        super.onDestroy()
        billingClient.endConnection()
    }
}

