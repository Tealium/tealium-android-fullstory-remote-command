package com.tealium.remotecommands.fullstory

object Commands {
    const val COMMAND_KEY = "command_name"
    const val SEPARATOR = ","

    const val LOG_EVENT = "logevent"
    const val IDENTIFY = "identify"
    const val SET_USER_VARIABLES = "setuservariables"
    const val SHUTDOWN = "shutdown"
    const val RESTART = "restart"
    const val ANONYMIZE = "anonymize"
    const val CONSENT = "consent"
    const val RESET_IDLE_TIMER = "resetidletimer"
    const val LOG = "log"
}

object Keys {
    const val EVENT_NAME = "event_name"
    const val EVENT_PROPERTIES = "event"

    const val UID = "uid"
    const val USER_VARIABLES = "user_variables"

    const val CONSENT_GRANTED = "consent_granted"

    const val LOG_LEVEL = "log_level"
    const val LOG_MESSAGE = "log_message"
}