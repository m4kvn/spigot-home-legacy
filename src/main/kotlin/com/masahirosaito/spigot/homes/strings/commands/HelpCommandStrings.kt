package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.datas.strings.commands.HelpCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
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

    fun CONSOLE_COMMAND_LIST() = buildString {
        append("&6[ Homes console command list ]&r\n")
        append("&dhome help <command_name> : ${USAGE_HELP_COMMAND()}&r\n")
        HomeCommand.homeCommand.consoleSubCommands.forEach {
            append("  &b${it.name}&r : ${it.description}\n")
        }
    }
}
