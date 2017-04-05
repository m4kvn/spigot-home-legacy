package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.strings.Strings.COMMAND_NAME
import com.masahirosaito.spigot.homes.strings.Strings.HOME_LIMIT_NUM
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.strings.Strings.PERMISSION_NAME
import com.masahirosaito.spigot.homes.strings.Strings.PLAYER_NAME
import com.masahirosaito.spigot.homes.datas.strings.ErrorStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import com.masahirosaito.spigot.homes.strings.Strings.COMMAND_USAGE
import java.io.File

object ErrorStrings {
    lateinit var error: ErrorStringData

    fun load(folderPath: String) {
        error = loadData(File(folderPath, "error.json").load(), ErrorStringData::class.java)
    }

    fun NO_COMMAND(commandName: String) =
            error.NO_COMMAND
                    .replace(COMMAND_NAME, commandName)

    fun NO_CONSOLE_COMMAND() =
            error.NO_CONSOLE_COMMAND

    fun NO_PERMISSION(permission: String) =
            error.NO_PERMISSION
                    .replace(PERMISSION_NAME, permission)

    fun NO_ONLINE_PLAYER(playerName: String) =
            error.NO_ONLINE_PLAYER
                    .replace(PLAYER_NAME, playerName)

    fun NO_OFFLINE_PLAYER(playerName: String) =
            error.NO_OFFLINE_PLAYER
                    .replace(PLAYER_NAME, playerName)

    fun NO_VAULT() =
            error.NO_VAULT

    fun NO_ECONOMY() =
            error.NO_ECONOMY

    fun NO_DEFAULT_HOME(playerName: String) =
            error.NO_DEFAULT_HOME
                    .replace(PLAYER_NAME, playerName)

    fun NO_NAMED_HOME(playerName: String, homeName: String) =
            error.NO_NAMED_HOME
                    .replace(PLAYER_NAME, playerName)
                    .replace(HOME_NAME, homeName)

    fun NO_HOME(playerName: String) =
            error.NO_HOME
                    .replace(PLAYER_NAME, playerName)

    fun HOME_LIMIT(limit: Int) =
            error.HOME_LIMIT
                    .replace(HOME_LIMIT_NUM, limit.toString())

    fun DEFAULT_HOME_IS_PRIVATE(playerName: String) =
            error.DEFAULT_HOME_IS_PRIVATE
                    .replace(PLAYER_NAME, playerName)

    fun NAMED_HOME_IS_PRIVATE(playerName: String, homeName: String) =
            error.NAMED_HOME_IS_PRIVATE
                    .replace(PLAYER_NAME, playerName)
                    .replace(HOME_NAME, homeName)

    fun NO_RECEIVED_INVITATION() =
            error.NO_RECEIVED_INVITATION

    fun ALREADY_HAS_INVITATION(playerName: String) =
            error.ALREADY_HAS_INVITATION
                    .replace(PLAYER_NAME, playerName)

    fun NOT_ALLOW_BY_CONFIG() =
            error.NOT_ALLOW_BY_CONFIG

    fun ARGUMENT_INCORRECT(commandUsage: String) =
            error.ARGUMENT_INCORRECT
                    .replace(COMMAND_USAGE, commandUsage)

    fun INVALID_COMMAND_SENDER() =
            error.INVALID_COMMAND_SENDER
}
