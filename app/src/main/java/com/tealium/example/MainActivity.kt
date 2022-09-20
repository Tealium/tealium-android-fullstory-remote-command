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
            TealiumHelper.trackEvent("identify", mapOf("uid" to text))
        }

        trackEventButton.setOnClickListener {
            TealiumHelper.trackEvent("tealiumSampleEvent", emptyMap())
        }

        trackEventWithDataButton.setOnClickListener {
            TealiumHelper.trackEvent("tealiumSampleEventWithData", mapOf("sampleKey1" to "sampleValue1", "sampleKey2" to "sampleValue2"))
        }
    }
}