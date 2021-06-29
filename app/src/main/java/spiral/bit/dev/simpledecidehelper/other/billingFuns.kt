package spiral.bit.dev.simpledecidehelper.other

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import spiral.bit.dev.simpledecidehelper.R
import java.io.IOException

fun Context.verifyValidSignature(signedData: String, signature: String): Boolean {
    return try {
        val base64Key = getString(R.string.ke)
        Security.verifyPurchase(base64Key, signedData, signature)
    } catch (e: IOException) {
        false
    }
}

infix fun AppCompatActivity.initiatePurchase(billingClient: BillingClient) {
    val skuList = arrayListOf(
        ITEM_SKU_SUBSCRIBE
    )
    val params =
        SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
    val isBillSupportedResult =
        billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
    if (isBillSupportedResult.responseCode == BillingClient.BillingResponseCode.OK) {
        billingClient.querySkuDetailsAsync(
            params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList[0])
                        .build()
                    billingClient.launchBillingFlow(this, flowParams)
                } else this showToast getString(R.string.sub_not_found)
            } else this showToast " Ошибка - ${billingResult.debugMessage}"
        }
    } else {
        this showToast getString(R.string.subs_not_supported)
    }
}

infix fun AppCompatActivity.subscribe(billingClient: BillingClient) {
    if (billingClient.isReady) {
        this initiatePurchase billingClient
    } else {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    this@subscribe initiatePurchase billingClient
                } else {
                    applicationContext showToast "Ошибка - ${billingResult.debugMessage}"
                }
            }

            override fun onBillingServiceDisconnected() {
            }
        })
    }
}

fun AppCompatActivity.handlePurchases(
    purchases: List<Purchase>,
    billingClient: BillingClient,
    defSharedPrefs: SharedPreferences,
    ackPurchaseListener: AcknowledgePurchaseResponseListener
) {
    for (purchase in purchases) {
        if (ITEM_SKU_SUBSCRIBE == purchase.skus[0] && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                this showToast getString(R.string.error_invalid_sub)
                return
            }
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(
                    acknowledgePurchaseParams,
                    ackPurchaseListener
                )
            } else {
                if (!defSharedPrefs.getSubscribeValueFromPref()) {
                    defSharedPrefs saveSubscribeValueToPref true
                    this showToast getString(R.string.sub_confirmed)
                    recreate()
                }
            }
        } else if (ITEM_SKU_SUBSCRIBE == purchase.skus[0] && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            this showToast getString(R.string.sub_in_process)
        }
    }
}