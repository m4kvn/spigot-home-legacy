package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand.Companion.homeCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.datas.strings.commands.HelpCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import org.bukkit.entity.Player
import java.io.File

object HelpCommandStrings {
    lateinit private var data: HelpCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "help-command.json").load(), HelpCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_HELP() =
            data.USAGE_HELP

    fun USAGE_HELP_COMMAND() =
            data.USAGE_HELP_COMMAND

    fun PLAYER_COMMAND_LIST(player: Player) = buildString {
        append("&6Homes command list&r\n")
        append("&d/home help <command_name> : ${USAGE_HELP_COMMAND()}&r\n")
        homeCommand.playerSubCommands.forEach {
            if (it is PlayerCommand && it.hasPermission(player))
                append("  &b${it.name}&r : ${it.description}\n")
        }
        if (homeCommand.hasPermission(player))
            append("  &b${homeCommand.name}&r : ${homeCommand.description}\n")
    }

    fun CONSOLE_COMMAND_LIST() = buildString {
        append("&6[ Homes console command list ]&r\n")
        append("&dhome help <command_name> : ${USAGE_HELP_COMMAND()}&r\n")
        HomeCommand.homeCommand.consoleSubCommands.forEach {
            append("  &b${it.name}&r : ${it.description}\n")
        }
    }
}
