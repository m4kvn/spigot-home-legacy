package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand.Companion.homeCommand
import com.masahirosaito.spigot.homes.datas.strings.commands.HelpCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import org.bukkit.entity.Player
import java.io.File

object HelpCommandStrings {
    private const val FILE_NAME = "help-command.json"
    private lateinit var data: HelpCommandStringData

    val DESCRIPTION: String get() = data.DESCRIPTION
    val USAGE_HELP: String get() = data.USAGE_HELP
    val USAGE_HELP_COMMAND: String get() = data.USAGE_HELP_COMMAND

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { HelpCommandStringData() }
    }

    fun createPlayerCommandListMessage(player: Player) = buildString {
        append("&6Homes command list&r\n")
        append("&d/home help <command_name> : ${USAGE_HELP_COMMAND}&r\n")
        homeCommand.playerSubCommands.forEach {
            if (it.hasPermission(player))
                append("  &b${it.name}&r : ${it.description}\n")
        }
        if (homeCommand.hasPermission(player)) {
            append("  &b${homeCommand.name}&r : ${homeCommand.description}\n")
        }
    }

    fun createConsoleCommandListMessage() = buildString {
        append("&6[ Homes console command list ]&r\n")
        append("&dhome help <command_name> : ${USAGE_HELP_COMMAND}&r\n")
        homeCommand.consoleSubCommands.forEach {
            append("  &b${it.name}&r : ${it.description}\n")
        }
    }
}
