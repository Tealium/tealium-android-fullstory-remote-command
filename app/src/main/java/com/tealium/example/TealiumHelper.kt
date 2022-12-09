package com.tealium.example

import android.app.Application
import com.tealium.core.*
import com.tealium.dispatcher.TealiumEvent
import com.tealium.dispatcher.TealiumView
import com.tealium.remotecommanddispatcher.RemoteCommands
import com.tealium.remotecommanddispatcher.remoteCommands
import com.tealium.remotecommands.fullstory.FullStoryRemoteCommand

object TealiumHelper {

    const val INSTANCE = "main"

    fun initialize(application: Application) {
        val config = TealiumConfig(
            application,
            "tealiummobile",
            "android",
            Environment.DEV,
            dispatchers = mutableSetOf(
                Dispatchers.RemoteCommands
            )
        )

        Tealium.create(INSTANCE, config) {

            val fullstoryCommand = FullStoryRemoteCommand()
            remoteCommands?.add(fullstoryCommand, filename = "tealium-fullstory.json")
        }
    }

    fun trackView(name: String, data: Map<String, Any>? = null) {
        val viewDispatch = TealiumView(name, data)
        Tealium[INSTANCE]?.track(viewDispatch)
    }

    fun trackEvent(name: String, data: Map<String, Any>? = null) {
        val eventDispatch = TealiumEvent(name, data)
        Tealium[INSTANCE]?.track(eventDispatch)
    }
}