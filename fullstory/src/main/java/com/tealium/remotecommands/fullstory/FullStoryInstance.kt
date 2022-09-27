package com.tealium.remotecommands.fullstory

import com.fullstory.FS
import com.fullstory.FSOnReadyListener
import com.tealium.remotecommands.RemoteCommandContext

class FullStoryInstance(private val remoteCommandContext: RemoteCommandContext) : FullStoryCommand {
    override fun logEvent(eventName: String, eventData: Map<String, Any>?) {
        FS.event(eventName, eventData)
    }

    override fun identifyUser(id: String, data: Map<String, Any>?) {
        data?.let {
            FS.identify(id, it)
        } ?: run { FS.identify(id) }
    }

    override fun setUserData(data: Map<String, Any>) {
        FS.setUserVars(data)
    }

    override fun fetchSessionUrl() {
        remoteCommandContext.track("fullstory_session_url", mapOf("session_url" to FS.getCurrentSessionURL()))
    }
}