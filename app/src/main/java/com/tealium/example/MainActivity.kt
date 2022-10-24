package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        identifyUserButton.setOnClickListener {
            val text: String = userIdEditText.text.toString()
            TealiumHelper.trackEvent("identify_user", mapOf("uid" to text, "email" to "sample@domain.com", "phone" to "9999999999"))
        }

        trackEventButton.setOnClickListener {
            TealiumHelper.trackEvent("tealiumSampleEvent", emptyMap())
        }

        trackEventWithDataButton.setOnClickListener {
            TealiumHelper.trackEvent(
                "tealiumSampleEventWithData",
                mapOf("cart_id" to "12345", "product_id" to "54321", "price" to 5.99, "name" to "SampleEvent", "category" to mapOf("label" to "misc"))
            )
        }
    }
}