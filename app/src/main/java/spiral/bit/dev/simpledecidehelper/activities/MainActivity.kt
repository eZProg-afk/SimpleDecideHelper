package spiral.bit.dev.simpledecidehelper.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.SkuType.SUBS
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.ActivityMainBinding
import spiral.bit.dev.simpledecidehelper.fragments.CompletedTasksFragment
import spiral.bit.dev.simpledecidehelper.fragments.MyTasksFragment
import spiral.bit.dev.simpledecidehelper.listeners.ComplDismissListener
import spiral.bit.dev.simpledecidehelper.listeners.DismissListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.other.*
import spiral.bit.dev.simpledecidehelper.other.Security.verifyPurchase
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), DismissListener,
    ComplDismissListener, PurchasesUpdatedListener {

    private val decisionsBinding: ActivityMainBinding by viewBinding()

    @Inject
    lateinit var myTasksFragment: MyTasksFragment
    @Inject
    lateinit var complTasksFragment: CompletedTasksFragment
    @Inject
    lateinit var decisionBottomSheet: DecisionBottomSheet
    @Inject
    lateinit var completeBottomSheet: CompleteBottomSheet

    private lateinit var billingClient: BillingClient
    private lateinit var defSharedPrefs: SharedPreferences
    private lateinit var toggle: ImageView
    val decisionsViewModel: MainViewModel by viewModels()
    private var decisionsList = arrayListOf<Decision>()
    private var isOpenedDrawer: Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ACTIVITY = this
        initViews()
        startWorkManager()

        decisionsViewModel.allDecisions.observe(this, {
            decisionsList = it as ArrayList<Decision>
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initViews() {
        defSharedPrefs = getSharedPreferences("def_prefs", 0)
        billingClient =
            BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryPurchase = billingClient.queryPurchases(SUBS)
                    val queryPurchases = queryPurchase.purchasesList
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        handlePurchases(queryPurchases)
                    } else saveSubscribeValueToPref(defSharedPrefs, false)
                }
            }

            override fun onBillingServiceDisconnected() {
            }
        })

        decisionsBinding.purchaseProBtn.setOnClickListener {
            decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
        }

        if (getSubscribeValueFromPref(defSharedPrefs)) decisionsBinding.adView.visibility =
            View.GONE
        else {
            decisionsBinding.adView.visibility = View.VISIBLE
            decisionsBinding.adView.loadAd(AdManagerAdRequest.Builder().build())
        }

        myTasksFragment.setModalBottomSheet(decisionBottomSheet)
        decisionBottomSheet.setMyDismissListener(this)
        completeBottomSheet.setMyDismissListener(this)

        decisionsBinding.tabBar.onItemSelected = {
            when (it) {
                0 -> {
                    changeFragments(myTasksFragment, true)
                    decisionsBinding.fabAddTask.setImageResource(R.drawable.ic_add_pencil)
                }
                1 -> {
                    changeFragments(complTasksFragment, true)
                    decisionsBinding.fabAddTask.setImageResource(R.drawable.ic_delete)
                }
            }
        }

        changeFragments(myTasksFragment, true)

        decisionsBinding.fabAddTask.setOnClickListener {
            val fragment =
                supportFragmentManager.findFragmentById(decisionsBinding.mainFragmentContainer.id)!!
            if (fragment is MyTasksFragment)
                decisionBottomSheet.show(supportFragmentManager, TAGMODALBOTTOMSHEET)
            else if (fragment is CompletedTasksFragment)
                completeBottomSheet.show(supportFragmentManager, TAGMODALBOTTOMSHEET)
        }

        decisionsBinding.tabBar.onItemReselected = { }
        initDrawer()
    }

    private fun startWorkManager() {
        val repeatInterval = defSharedPrefs.getInt("days_auto_delete", 0).toLong()
        if (repeatInterval == 100L) {
            return
        } else {
            val workRequest = PeriodicWorkRequest.Builder(
                RoomWorker::class.java, repeatInterval, TimeUnit.DAYS
            )
                .build()
            val workManager = WorkManager.getInstance(this)
            workManager.enqueue(workRequest)
        }
    }


    @SuppressLint("RtlHardcoded")
    private fun initDrawer() {
        toggle = decisionsBinding.hambMenu
        toggle.setOnClickListener {
            isOpenedDrawer = if (isOpenedDrawer) {
                decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                false
            } else {
                decisionsBinding.drawerLayout.openDrawer(Gravity.LEFT)
                true
            }

            decisionsBinding.navigationView.requestFocus()
            decisionsBinding.navigationView.bringToFront()
            if (getSubscribeValueFromPref(defSharedPrefs))
                decisionsBinding.navigationView.menu.findItem(R.id.buy_pro_version)
                    .isVisible = false
            decisionsBinding.navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.completed_decisions -> {
                        decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                        decisionsBinding.tabBar.itemActiveIndex = 1
                        changeFragments(complTasksFragment, true)
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.rate_app -> {
                        decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                        val uri: Uri = Uri.parse("market://details?id=$packageName")
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        try {
                            startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                                )
                            )
                        }
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.invite_a_friend -> {
                        decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                        val sendIntent: Intent = Intent().apply {
                            val uri: Uri = Uri.parse("market://details?id=$packageName")
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Привет! Скачай это крутое приложение пожалуйста!))" +
                                        "\n Вот ссылка -   $uri"
                            )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.night_theme -> {
                        decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                        val currentMode = it.isChecked
                        if (currentMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        return@setNavigationItemSelectedListener true
                    }
                    R.id.buy_pro_version -> {
                        decisionsBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                        subscribe()
                        return@setNavigationItemSelectedListener true
                    }
                    else -> {
                        return@setNavigationItemSelectedListener false
                    }
                }
            }
        }
    }

    override fun dismiss() {
        decisionBottomSheet.dismiss()
    }

    override fun editDismiss() {
        completeBottomSheet.dismiss()
    }

    //BILLING

    private fun subscribe() {
        if (billingClient.isReady) initiatePurchase()
        else {
            billingClient =
                BillingClient.newBuilder(this).enablePendingPurchases().setListener(this)
                    .build()
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Ошибка - " + billingResult.debugMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onBillingServiceDisconnected() {
                }
            })
        }
    }

    private fun initiatePurchase() {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(ITEM_SKU_SUBSCRIBE)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(SUBS)
        val billResult =
            billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (billResult.responseCode == BillingClient.BillingResponseCode.OK) {
            billingClient.querySkuDetailsAsync(
                params.build()
            ) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (skuDetailsList != null && skuDetailsList.size > 0) {
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList[0])
                            .build()
                        billingClient.launchBillingFlow(this@MainActivity, flowParams)
                    } else
                        Toast.makeText(
                            applicationContext,
                            "Подписка не найдена :(",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                } else
                    Toast.makeText(
                        applicationContext,
                        " Ошибка - " + billingResult.debugMessage, Toast.LENGTH_SHORT
                    ).show()
            }
        } else Toast.makeText(
            applicationContext,
            "Извините, подписки не подерживаются. Пожалуйста, обновите Google Play Store.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            if (ITEM_SKU_SUBSCRIBE == purchase.skus[0] && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                    Toast.makeText(
                        applicationContext,
                        "Ошибка - невалидная покупка",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
                } else {
                    if (!getSubscribeValueFromPref(defSharedPrefs)) {
                        saveSubscribeValueToPref(defSharedPrefs, true)
                        Toast.makeText(
                            applicationContext,
                            "Подписка успешно оформлена! Перезапустите приложение!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        recreate()
                    }
                }
            } else if (ITEM_SKU_SUBSCRIBE == purchase.skus[0] && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                Toast.makeText(
                    applicationContext,
                    "Покупка находится в процессе. Пожалуйста, завершите транзакцию",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            val base64Key = getString(R.string.ke)
            verifyPurchase(base64Key, signedData, signature)
        } catch (e: IOException) {
            false
        }
    }

    var ackPurchase =
        AcknowledgePurchaseResponseListener { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                saveSubscribeValueToPref(defSharedPrefs, true)
                recreate()
            }
        }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult = billingClient.queryPurchases(SUBS)
            val alreadyPurchases = queryAlreadyPurchasesResult.purchasesList
            alreadyPurchases?.let { handlePurchases(it) }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(applicationContext, "Подписка отменена", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                applicationContext,
                "Error " + billingResult.debugMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        billingClient.endConnection()
    }
}

