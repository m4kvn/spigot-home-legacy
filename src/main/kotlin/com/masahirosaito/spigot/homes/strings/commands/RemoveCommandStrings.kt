package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.datas.strings.commands.RemoveCommandStringData
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object RemoveCommandStrings {
    lateinit private var data: RemoveCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "remove-command.json"), RemoveCommandStringData::class.java)
    }

    fun REMOVE_DEFAULT_HOME() =
            data.REMOVE_DEFAULT_HOME

    fun REMOVE_NAMED_HOME(homeName: String) =
            data.REMOVE_NAMED_HOME
                    .replace(HOME_NAME, homeName)
}
