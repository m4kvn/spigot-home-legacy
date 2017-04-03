package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.ListCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object ListCommandStrings {
    lateinit private var data: ListCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "list-command.json").load(), ListCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_LIST() =
            data.USAGE_LIST

    fun USAGE_LIST_PLAYER() =
            data.USAGE_LIST_PLAYER
}
