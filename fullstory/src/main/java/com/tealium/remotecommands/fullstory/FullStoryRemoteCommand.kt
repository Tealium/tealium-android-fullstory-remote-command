package com.tealium.remotecommands.fullstory

import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.RemoteCommandContext
import org.json.JSONObject
import java.util.*

class FullStoryRemoteCommand(
    commandId: String = DEFAULT_COMMAND_ID,
    commandDescription: String = DEFAULT_COMMAND_DESCRIPTION
) : RemoteCommand(commandId, commandDescription, BuildConfig.TEALIUM_FULLSTORY_VERSION) {

    lateinit var fullStoryInstance: FullStoryCommand

    private val TAG = this::class.java.simpleName


    override fun onInvoke(response: Response) {
        val payload = response.requestPayload
        val commands = splitCommands(payload)
        parseCommands(commands, payload)
    }

    fun parseCommands(commands: Array<String>, payload: JSONObject) {
        commands.forEach { command ->
            println("Executing command: $command")
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
                Commands.FETCH_SESSION_URL -> {
                    fullStoryInstance.fetchSessionUrl()
                }
            }
        }
    }

    private fun identify(json: JSONObject) {
        val uid: String = json.optString(Keys.UID)
        val userData = json.optJSONObject(Keys.USER_VARIABLES)
        if (uid.isNotBlank()) {
            userData?.let {
                fullStoryInstance.identifyUser(uid,it.toTypedMap())
            } ?: fullStoryInstance.identifyUser(uid)
        }
    }

    private fun setUserVariables(json: JSONObject) {
        val userData = json.optJSONObject(Keys.USER_VARIABLES)
        userData?.let {
            fullStoryInstance.setUserData(it.toTypedMap())
        }
    }

    private fun logEvent(json: JSONObject) {
        val eventName = json.optString(Keys.EVENT_NAME)
        val eventData = json.optJSONObject(Keys.EVENT_PROPERTIES)
        eventData?.let {
            fullStoryInstance.logEvent(eventName, it.toTypedMap())
        } ?: fullStoryInstance.logEvent(eventName)
    }

    private inline fun <reified T> JSONObject.toTypedMap(): Map<String, T> {
        val map = HashMap<String, T>()
        keys().forEach { key ->
            val value = this[key]
            (value as? T)?.let {
                map[key] = value
            }
        }
        return map
    }

    internal fun splitCommands(payload: JSONObject): Array<String> {
        val command = payload.optString(Commands.COMMAND_KEY, "")
        return command.split(Commands.SEPARATOR).map {
            it.trim().lowercase()
        }.toTypedArray()
    }

    override fun setContext(context: RemoteCommandContext?) {
        context?.let {
            fullStoryInstance = FullStoryInstance(it)
        }
    }

    companion object {
        const val DEFAULT_COMMAND_ID = "fullstory"
        const val DEFAULT_COMMAND_DESCRIPTION = "Tealium-FullStory Remote Command"
    }
}