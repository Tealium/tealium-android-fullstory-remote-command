package com.tealium.remotecommands.fullstory

import com.fullstory.FS

class FullStoryInstance : FullStoryCommand {
    override fun logEvent(eventName: String, eventData: Map<String, Any>?) {
        FS.event(eventName, eventData)
    }

    override fun identifyUser(id: String, data: Map<String, Any>?) {
        FS.identify(id, data)
    }

    override fun setUserData(data: Map<String, Any>) {
        FS.setUserVars(data)
    }
}