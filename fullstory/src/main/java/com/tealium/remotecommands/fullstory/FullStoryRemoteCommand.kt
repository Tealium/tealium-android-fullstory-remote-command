package com.tealium.remotecommands.fullstory

import android.util.Log
import com.tealium.remotecommands.RemoteCommand
import java.util.Locale
import org.json.JSONArray
import org.json.JSONObject

class FullStoryRemoteCommand(
    private val fullStoryInstance: FullStoryCommand = FullStoryInstance(),
    commandId: String = DEFAULT_COMMAND_ID,
    commandDescription: String = DEFAULT_COMMAND_DESCRIPTION
) : RemoteCommand(commandId, commandDescription, BuildConfig.TEALIUM_FULLSTORY_VERSION) {

    override fun onInvoke(response: Response) {
        val payload = response.requestPayload
        val commands = splitCommands(payload)
        parseCommands(commands, payload)
    }

    fun parseCommands(commands: Array<String>, payload: JSONObject) {
        commands.forEach { command ->
            Log.d(BuildConfig.TAG, "Processing command: $command with payload: $payload")
            when (command) {
                Commands.IDENTIFY -> identify(payload)
                Commands.SET_USER_VARIABLES -> setUserVariables(payload)
                Commands.LOG_EVENT -> logEvent(payload)
                Commands.SHUTDOWN -> fullStoryInstance.shutdown()
                Commands.RESTART -> fullStoryInstance.restart()
                Commands.ANONYMIZE -> fullStoryInstance.anonymize()
                Commands.CONSENT -> consent(payload)
                Commands.RESET_IDLE_TIMER -> fullStoryInstance.resetIdleTimer()
                Commands.LOG -> log(payload)
                else -> Log.w(BuildConfig.TAG, "Unknown command: $command")
            }
        }
    }

    private fun identify(json: JSONObject) {
        val uid: String = json.optString(Keys.UID)
        val userData = json.optJSONObject(Keys.USER_VARIABLES)
        if (uid.isNotBlank()) {
            fullStoryInstance.identifyUser(uid, userData?.convertToMap())
        }
    }

    private fun setUserVariables(json: JSONObject) {
        val userData = json.optJSONObject(Keys.USER_VARIABLES)
        userData?.let {
            fullStoryInstance.setUserData(it.convertToMap())
        }
    }

    private fun logEvent(json: JSONObject) {
        val eventName = json.optString(Keys.EVENT_NAME)
        if (eventName.isBlank()) return
        val eventData = json.optJSONObject(Keys.EVENT_PROPERTIES)?.convertToMap()
        fullStoryInstance.logEvent(eventName, eventData)
    }

    private fun consent(json: JSONObject) {
        val consentGranted = json.opt(Keys.CONSENT_GRANTED) as? Boolean ?: return
        fullStoryInstance.consent(consentGranted)
    }

    private fun log(json: JSONObject) {
        val level = json.optString(Keys.LOG_LEVEL).toFSLogLevel() ?: return
        val message = json.optString(Keys.LOG_MESSAGE).takeIf { it.isNotBlank() } ?: return
        fullStoryInstance.log(level, message)
    }

    private fun JSONObject.convertToMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val iterator = keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            when (val value = this[key]) {
                is JSONObject -> {
                    map[key] = value.convertToMap()
                }
                is JSONArray -> {
                    map[key] = value.convertToList()
                }
                else -> {
                    map[key] = value
                }
            }
        }
        return map.toMap()
    }

    private fun JSONArray.convertToList(): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until length()) {
            when (val value = this[i]) {
                is JSONObject -> {
                    list.add(value.convertToMap())
                }
                is JSONArray -> {
                    list.add(value.convertToList())
                }
                is Boolean, is Int, is Double, is String -> {
                    list.add(value)
                }
                else -> {
                    list.add(value.toString())
                }
            }
        }
        return list.toList()
    }

    internal fun splitCommands(payload: JSONObject): Array<String> {
        val command = payload.optString(Commands.COMMAND_KEY, "")
        return command.split(Commands.SEPARATOR).map {
            it.trim().lowercase(Locale.ROOT)
        }.toTypedArray()
    }

    companion object {
        const val DEFAULT_COMMAND_ID = "fullstory"
        const val DEFAULT_COMMAND_DESCRIPTION = "Tealium-FullStory Remote Command"
    }
}