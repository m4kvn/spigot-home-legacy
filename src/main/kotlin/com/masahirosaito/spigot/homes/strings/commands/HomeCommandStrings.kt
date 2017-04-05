package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.HomeCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object HomeCommandStrings {
    lateinit private var data: HomeCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "home-command.json").load(), HomeCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_HOME() =
            data.USAGE_HOME

    fun USAGE_HOME_NAME() =
            data.USAGE_HOME_NAME

    fun USAGE_HOME_PLAYER() =
            data.USAGE_HOME_PLAYER

    fun USAGE_HOME_NAME_PLAYER() =
            data.USAGE_HOME_NAME_PLAYER
}
