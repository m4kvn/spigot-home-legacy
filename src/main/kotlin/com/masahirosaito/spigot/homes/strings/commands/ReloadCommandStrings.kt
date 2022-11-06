package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.ReloadCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import java.io.File

object ReloadCommandStrings {
    private const val FILE_NAME = "reload-command.json"
    private lateinit var data: ReloadCommandStringData

    val DESCRIPTION: String get() = data.DESCRIPTION
    val USAGE_RELOAD: String get() = data.USAGE_RELOAD

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { ReloadCommandStringData() }
    }
}
