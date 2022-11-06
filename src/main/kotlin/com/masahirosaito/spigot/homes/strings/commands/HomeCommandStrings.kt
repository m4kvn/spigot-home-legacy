package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.HomeCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import java.io.File

object HomeCommandStrings {
    private const val FILE_NAME = "home-command.json"
    private lateinit var data: HomeCommandStringData

    val DESCRIPTION: String get() = data.DESCRIPTION
    val USAGE_HOME: String get() = data.USAGE_HOME
    val USAGE_HOME_NAME: String get() = data.USAGE_HOME_NAME
    val USAGE_HOME_PLAYER: String get() = data.USAGE_HOME_PLAYER
    val USAGE_HOME_NAME_PLAYER: String get() = data.USAGE_HOME_NAME_PLAYER

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { HomeCommandStringData() }
    }
}
