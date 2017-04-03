package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.Strings.PLAYER_NAME
import com.masahirosaito.spigot.homes.datas.strings.HomeDisplayStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object HomeDisplayStrings {
    lateinit private var data: HomeDisplayStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "display.json").load(), HomeDisplayStringData::class.java)
    }

    fun DEFAULT_HOME_DISPLAY_NAME(playerName: String) =
            data.DEFAULT_HOME_DISPLAY_NAME
                    .replace(PLAYER_NAME, playerName)

    fun NAMED_HOME_DISPLAY_NAME(playerName: String, homeName: String) =
            data.NAMED_HOME_DISPLAY_NAME
                    .replace(PLAYER_NAME, playerName)
                    .replace(HOME_NAME, homeName)
}
