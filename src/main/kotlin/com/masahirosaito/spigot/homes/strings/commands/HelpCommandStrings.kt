package com.masahirosaito.spigot.homes.strings.commands

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
}
