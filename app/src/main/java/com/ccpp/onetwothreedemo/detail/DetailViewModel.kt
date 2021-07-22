package com.ccpp.onetwothreedemo.detail

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ccpp.onetwothreedemo.R
import com.ccpp.onetwothreedemo.constant.Constants
import com.ccpp.onetwothreedemo.model.Product
import com.ccpp.onetwothreedemo.util.liveEvent
import com.ccpp.onetwothreesdk.callback.APIResponseCallback
import com.ccpp.onetwothreesdk.core.OneTwoThreeSDKService
import com.ccpp.onetwothreesdk.model.Buyer
import com.ccpp.onetwothreesdk.model.Merchant
import com.ccpp.onetwothreesdk.model.Transaction
import com.ccpp.onetwothreesdk.model.response.StartDeeplinkResponse
import com.google.gson.Gson
import java.util.*

enum class PaymentType {
    SCB, BAY, BBL
}

class DetailViewModel : ViewModel() {

    sealed class Event {
        data class OnPaySuccess(val res: StartDeeplinkResponse) : Event()
        data class OnPayFailed(val error: Throwable?) : Event()
    }
    var event = liveEvent<Event>()
    var isInProgress = MutableLiveData(false)

    var product: Product? = null

    val paymentMethod = MutableLiveData(PaymentType.SCB)
    val scbSelected = MutableLiveData(true)
    val baySelected = MutableLiveData(false)
    val bblSelected = MutableLiveData(false)

    init {
        paymentMethod.observeForever { type ->
            scbSelected.value = false
            baySelected.value = false
            bblSelected.value = false
            when(type) {
                PaymentType.SCB -> scbSelected.value = true
                PaymentType.BAY -> baySelected.value = true
                PaymentType.BBL -> bblSelected.value = true
            }
        }
    }

    fun selectBank(view: View) {
        when(view.id){
            R.id.scb -> paymentMethod.value = PaymentType.SCB
            R.id.bay -> paymentMethod.value = PaymentType.BAY
            R.id.bbl -> paymentMethod.value = PaymentType.BBL
        }
    }

    /**
     * Call when pressed pay button.
     */
    fun pay() {
        isInProgress.value = true

        // TODO: Add information for PAY
        val merchant = Merchant(
            id = "merchant@shopping.com",
            redirectURL = Constants.appScheme,
            notificationURL = "https=//uat2.123.co.th/DemoShopping/apicallurl.aspx",
            merchantData = listOf(
//                MerchantData(item = ""),
            )
        )

        val transaction = Transaction(
            merchantReference = UUID.randomUUID().toString().replace("-", "").substring(0,11),
            preferredAgent = "",
            productDesc = "Description of the product.",
            amount = "1",
            currencyCode = "THB",
            paymentInfo = "",
            paymentExpiry = ""
        )

        val buyer = Buyer(
            email = "",
            mobile = "",
            language = "EN",
            notifyBuyer = true,
            title = "",
            firstName = "",
            lastName = ""
        )

        /// Unit test with synchronous style
        OneTwoThreeSDKService.startDeeplink(merchant, transaction, buyer, object :
            APIResponseCallback<StartDeeplinkResponse> {
            override fun onResponse(response: StartDeeplinkResponse) {
                Log.d("API", "response => ${Gson().toJson(response)}")
                isInProgress.value = false
                event.value = Event.OnPaySuccess(response)
            }

            override fun onFailure(error: Throwable?) {
                isInProgress.value = false
                Log.d("API", "failure => ${error?.message}")
                event.value = Event.OnPayFailed(error)
            }
        })
    }

}
