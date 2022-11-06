package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.datas.strings.ErrorStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.strings.Strings.COMMAND_NAME
import com.masahirosaito.spigot.homes.strings.Strings.COMMAND_USAGE
import com.masahirosaito.spigot.homes.strings.Strings.HOME_LIMIT_NUM
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.strings.Strings.PERMISSION_NAME
import com.masahirosaito.spigot.homes.strings.Strings.PLAYER_NAME
import java.io.File

object ErrorStrings {
    private const val FILE_NAME = "error.json"
    private lateinit var error: ErrorStringData

    val NO_VAULT: String get() = error.NO_VAULT
    val NO_ECONOMY: String get() = error.NO_ECONOMY
    val NO_RECEIVED_INVITATION: String get() = error.NO_RECEIVED_INVITATION
    val NOT_ALLOW_BY_CONFIG: String get() = error.NOT_ALLOW_BY_CONFIG
    val INVALID_COMMAND_SENDER: String get() = error.INVALID_COMMAND_SENDER
    val ALREADY_EXECUTE_TELEPORT: String get() = error.ALREADY_EXECUTE_TELEPORT
    val NO_CONSOLE_COMMAND: String get() = error.NO_CONSOLE_COMMAND

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        error = loadDataAndSave(file) { ErrorStringData() }
    }

    fun createNoCommandMessage(commandName: String) =
        error.NO_COMMAND
            .replace(COMMAND_NAME, commandName)

    fun createNoPermissionMessage(permission: String) =
        error.NO_PERMISSION
            .replace(PERMISSION_NAME, permission)

    fun createNoOnlinePlayerMessage(playerName: String) =
        error.NO_ONLINE_PLAYER
            .replace(PLAYER_NAME, playerName)

    fun createNoOfflinePlayerMessage(playerName: String) =
        error.NO_OFFLINE_PLAYER
            .replace(PLAYER_NAME, playerName)

    fun createNoDefaultHomeMessage(playerName: String) =
        error.NO_DEFAULT_HOME
            .replace(PLAYER_NAME, playerName)

    fun createNoNamedHome(playerName: String, homeName: String) =
        error.NO_NAMED_HOME
            .replace(PLAYER_NAME, playerName)
            .replace(HOME_NAME, homeName)

    fun createNoHome(playerName: String) =
        error.NO_HOME
            .replace(PLAYER_NAME, playerName)

    fun createHomeLimitMessage(limit: Int) =
        error.HOME_LIMIT
            .replace(HOME_LIMIT_NUM, limit.toString())

    fun createDefaultHomeIsPrivate(playerName: String) =
        error.DEFAULT_HOME_IS_PRIVATE
            .replace(PLAYER_NAME, playerName)

    fun createNamedHomeIsPrivate(playerName: String, homeName: String) =
        error.NAMED_HOME_IS_PRIVATE
            .replace(PLAYER_NAME, playerName)
            .replace(HOME_NAME, homeName)

    fun createAlreadyHasInvitation(playerName: String) =
        error.ALREADY_HAS_INVITATION
            .replace(PLAYER_NAME, playerName)

    fun createArgumentIncorrect(commandUsage: String) =
        error.ARGUMENT_INCORRECT
            .replace(COMMAND_USAGE, commandUsage)
}
