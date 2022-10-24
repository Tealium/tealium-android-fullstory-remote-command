package com.tealium.remotecommands.fullstory

import android.util.Log
import com.tealium.remotecommands.RemoteCommand
import org.json.JSONArray
import org.json.JSONObject

class FullStoryRemoteCommand(
    commandId: String = DEFAULT_COMMAND_ID,
    commandDescription: String = DEFAULT_COMMAND_DESCRIPTION
) : RemoteCommand(commandId, commandDescription, BuildConfig.TEALIUM_FULLSTORY_VERSION) {

    val fullStoryInstance: FullStoryCommand = FullStoryInstance()

    override fun onInvoke(response: Response) {
        val payload = response.requestPayload
        val commands = splitCommands(payload)
        parseCommands(commands, payload)
    }

    fun parseCommands(commands: Array<String>, payload: JSONObject) {
        commands.forEach { command ->
            Log.d(BuildConfig.TAG, "Processing command: $command with payload: $payload")
            when (command) {
                Commands.IDENTIFY -> {
                    identify(payload)
                }
                Commands.SET_USER_VARIABLES -> {
                    setUserVariables(payload)
                }
                Commands.LOG_EVENT -> {
                    logEvent(payload)
                }
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
        val eventData = json.optJSONObject(Keys.EVENT_PROPERTIES)
        fullStoryInstance.logEvent(eventName, eventData?.convertToMap())
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
            it.trim().lowercase()
        }.toTypedArray()
    }

    companion object {
        const val DEFAULT_COMMAND_ID = "fullstory"
        const val DEFAULT_COMMAND_DESCRIPTION = "Tealium-FullStory Remote Command"
    }
}