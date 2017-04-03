package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.ReloadCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object ReloadCommandStrings {
    lateinit private var data: ReloadCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "reload-command.json").load(), ReloadCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_RELOAD() =
            data.USAGE_RELOAD
}
