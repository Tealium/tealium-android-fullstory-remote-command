package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tealium.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.identifyUserButton.setOnClickListener {
            val text: String = binding.userIdEditText.text.toString()
            TealiumHelper.trackEvent("identify_user", mapOf("uid" to text, "email" to "sample@domain.com", "phone" to "9999999999"))
        }

        binding.trackEventButton.setOnClickListener {
            TealiumHelper.trackEvent("tealiumSampleEvent", emptyMap())
        }

        binding.trackEventWithDataButton.setOnClickListener {
            TealiumHelper.trackEvent(
                "tealiumSampleEventWithData",
                mapOf("cart_id" to "12345", "product_id" to "54321", "price" to 5.99, "name" to "SampleEvent", "category" to mapOf("label" to "misc"))
            )
        }
    }
}
