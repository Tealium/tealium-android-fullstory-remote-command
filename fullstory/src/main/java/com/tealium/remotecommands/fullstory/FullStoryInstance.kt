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

    override fun anonymize() {
        FS.anonymize()
    }

    override fun shutdown() {
        FS.shutdown()
    }

    override fun restart() {
        FS.restart()
    }

    override fun consent(userConsents: Boolean) {
        FS.consent(userConsents)
    }

    override fun resetIdleTimer() {
        FS.resetIdleTimer()
    }

    override fun log(logLevel: FS.LogLevel, message: String) {
        FS.log(logLevel, message)
    }
}