package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.datas.strings.commands.SetCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object SetCommandStrings {
    lateinit private var data: SetCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "set-command.json").load(), SetCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_SET() =
            data.USAGE_SET

    fun USAGE_SET_NAME() =
            data.USAGE_SET_NAME

    fun SET_DEFAULT_HOME() =
            data.SET_DEFAULT_HOME

    fun SET_NAMED_HOME(homeName: String) =
            data.SET_NAMED_HOME
                    .replace(HOME_NAME, homeName)
}
