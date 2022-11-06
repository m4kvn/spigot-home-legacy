package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.datas.strings.HomeDisplayStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.strings.Strings.PLAYER_NAME
import java.io.File

object HomeDisplayStrings {
    private const val FILE_NAME = "display.json"
    private lateinit var data: HomeDisplayStringData

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { HomeDisplayStringData() }
    }

    fun createDefaultHomeDisplayName(playerName: String) =
        data.DEFAULT_HOME_DISPLAY_NAME
            .replace(PLAYER_NAME, playerName)

    fun createNamedHomeDisplayName(playerName: String, homeName: String) =
        data.NAMED_HOME_DISPLAY_NAME
            .replace(PLAYER_NAME, playerName)
            .replace(HOME_NAME, homeName)
}
