package com.duc.karaoke_app.core.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*

class BillingManager(private val context: Context) {

    // LiveData để thông báo khi giao dịch mua VIP thành công
    private val _purchaseSuccess = SingleLiveEvent<Boolean>()
    val purchaseSuccess: LiveData<Boolean> get() = _purchaseSuccess
    private lateinit var billingClient: BillingClient
    private var skuDetails: SkuDetails? = null
    private val skuList = listOf("vip_monthly") // ID sản phẩm VIP từ Google Play Console
    // LiveData để thông báo trạng thái đang tải
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    // LiveData để thông báo lỗi
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    var purchaseTokenCache: String? = null

    // Listener để nhận cập nhật từ giao dịch mua
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }else if(billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
            _errorMessage.postValue("Giao dịch đã bị hủy")
        }else{
            _errorMessage.postValue("Lỗi giao dịch: ${billingResult.debugMessage}")
        }
        _isLoading.postValue(false)
    }
    init {
        setupBillingClient()
    }

    // Khởi tạo và kết nối với Google Billing Service
    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    querySkuDetails()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Xử lý khi mất kết nối (có thể thử kết nối lại)
            }
        })
    }

    // Truy vấn thông tin SKU từ Google Play
    private fun querySkuDetails() {
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)
            .build()

        billingClient.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                skuDetailsList != null && skuDetailsList.isNotEmpty()
            ) {
                skuDetails = skuDetailsList[0]
                Log.d("BillingManager", "Đã lấy thông tin sản phẩm: ${skuDetails?.title}, giá: ${skuDetails?.price}")
            }else {
                Log.e("BillingManager", "Không thể lấy thông tin sản phẩm: ${billingResult.debugMessage}")
                _errorMessage.postValue("Không thể lấy thông tin sản phẩm: ${billingResult.debugMessage}")
            }
            _isLoading.postValue(false)
        }
    }

    // Khởi chạy giao dịch mua
    fun launchPurchaseFlow(activity: Activity) {
        if (!::billingClient.isInitialized || !billingClient.isReady) {
            _errorMessage.postValue("Google Play Billing chưa sẵn sàng. Vui lòng thử lại sau.")
            setupBillingClient()
            return
        }
        skuDetails?.let { details ->
            _isLoading.postValue(true)
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(details)
                .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams).responseCode
            if (responseCode != BillingClient.BillingResponseCode.OK) {
                _errorMessage.postValue("Không thể bắt đầu giao dịch: $responseCode")
                _isLoading.postValue(false)
            }
        }?: run{
            _errorMessage.postValue("Thông tin sản phẩm chưa sẵn sàng. Vui lòng thử lại sau.")
            querySkuDetails()
        }
    }

    // Xử lý giao dịch khi người dùng mua thành công
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            Log.d("BillingManager", "Purchase token: ${purchase.purchaseToken}")
            purchaseTokenCache = purchase.purchaseToken
            Log.d("BillingManager", "Purchase token cache: ${purchaseTokenCache}")
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        _purchaseSuccess.postValue(true)
                        Log.d("BillingManager", "Giao dịch thành công và đã được xác nhận")
                    } else {
                        _errorMessage.postValue("Không thể xác nhận giao dịch: ${billingResult.debugMessage}")
                    }
                    _isLoading.postValue(false)
                }
            } else {
                // Nếu giao dịch đã được xác nhận rồi
                _purchaseSuccess.postValue(true)
                _isLoading.postValue(false)
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            _errorMessage.postValue("Giao dịch đang chờ xử lý")
            _isLoading.postValue(false)
        } else {
            _errorMessage.postValue("Giao dịch không thành công")
            _isLoading.postValue(false)
        }
    }

    // Kiểm tra trạng thái VIP hiện tại (nếu người dùng đã mua trước đó)
    fun checkVipStatus() {
        Log.d("BillingManager", "📦Đã vào checkvip")
        if (!::billingClient.isInitialized || !billingClient.isReady) {
            Log.w("BillingManager", "BillingClient chưa sẵn sàng")
            setupBillingClient()
            return
        }
        _isLoading.postValue(true)
        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { billingResult, purchases ->
            Log.d("BillingManager", "📦 Số lượng gói mua: ${purchases?.size ?: 0}")
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases?.let {
                    for (purchase in it) {
                        if (purchase.skus.contains("vip_monthly") && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            purchaseTokenCache = purchase.purchaseToken
                            Log.d("BillingManager", "token, token: ${purchaseTokenCache}")
                            Log.d("BillingManager", "Đã mua sản phẩm vip_monthly, purchaseToken: ${purchase.purchaseToken}")
                        }
                    }
                    val isVip = it.any { purchase ->
                        purchase.skus.contains("vip_monthly") && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    }
                    if (isVip) {
                        _purchaseSuccess.postValue(true)
                    } else {
                        _purchaseSuccess.postValue(false)
                    }
                }
            } else {
                Log.e("BillingManager", "Không thể kiểm tra giao dịch: ${billingResult.debugMessage}")
            }
            _isLoading.postValue(false)
        }
    }
}