package com.example.mykoinapp.presentation.orders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.razorpay.PaymentResultListener

class PaymentActivity : ComponentActivity(), PaymentResultListener {
    val currentTimestamp = "ORD : ${System.currentTimeMillis()}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val amount = intent?.getDoubleExtra("AMOUNT", 0.0) ?: 0
              startPayment(amount.toInt())
    }

    private fun startPayment(amount: Int) {
        val checkout = com.razorpay.Checkout()
        checkout.setKeyID("rzp_test_7nhnJ4IdieM0aT") //  your Razorpay key

        try {
            val options = org.json.JSONObject()
            options.put("name", "Meal Cooker")
            options.put("description", "Payment for Order #${currentTimestamp}")
            options.put("currency", "INR")
            options.put("amount", amount * 100) // Convert to paise

            val prefill = org.json.JSONObject()
            prefill.put("email", "nimapsports@gmail.com")
            prefill.put("contact", "9876543210")
            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            e.printStackTrace()
            setResult(Activity.RESULT_CANCELED, Intent().apply {
                putExtra("ERROR_MESSAGE", "Payment initiation failed")
            })
            finish()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()
        val intent = Intent().apply {
            putExtra("PAYMENT_ID", razorpayPaymentId)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onPaymentError(code: Int, description: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show()
        val intent = Intent().apply {
            putExtra("ERROR_MESSAGE", description)
        }
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}