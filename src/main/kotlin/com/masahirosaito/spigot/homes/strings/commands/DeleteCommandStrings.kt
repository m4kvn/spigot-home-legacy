package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.datas.strings.commands.DeleteCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object DeleteCommandStrings {
    lateinit private var data: DeleteCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "delete-command.json").load(), DeleteCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_DELETE() =
            data.USAGE_DELETE

    fun USAGE_DELETE_NAME() =
            data.USAGE_DELETE_NAME

    fun DELETE_DEFAULT_HOME() =
            data.DELETE_DEFAULT_HOME

    fun DELETE_NAMED_HOME(homeName: String) =
            data.DELETE_NAMED_HOME
                    .replace(HOME_NAME, homeName)
}
