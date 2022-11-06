package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.SetCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import java.io.File

object SetCommandStrings {
    private const val FILE_NAME = "set-command.json"
    private lateinit var data: SetCommandStringData

    val DESCRIPTION: String get() = data.DESCRIPTION
    val USAGE_SET: String get() = data.USAGE_SET
    val USAGE_SET_NAME: String get() = data.USAGE_SET_NAME
    val SET_DEFAULT_HOME: String get() = data.SET_DEFAULT_HOME

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { SetCommandStringData() }
    }

    fun createSetNamedHomeMessage(homeName: String) =
        data.SET_NAMED_HOME
            .replace(HOME_NAME, homeName)
}
