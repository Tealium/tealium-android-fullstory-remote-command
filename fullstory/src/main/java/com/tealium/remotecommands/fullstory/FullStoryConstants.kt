package com.tealium.remotecommands.fullstory

object Commands {
    const val COMMAND_KEY = "command_name"
    const val SEPARATOR = ","

    const val LOG_EVENT = "logevent"
    const val IDENTIFY = "identify"
    const val SET_USER_VARIABLES = "setuservariables"
    const val ANONYMIZE = "anonymize"
    const val RESTART = "restart"
    const val SHUTDOWN = "shutdown"
}

object Keys {
    const val EVENT_NAME = "event_name"
    const val EVENT_PROPERTIES = "event"
    const val EVENT_PARAMETERS = "event_parameters"

    const val UID = "uid"
    const val USER_VARIABLES = "user_variables"
}