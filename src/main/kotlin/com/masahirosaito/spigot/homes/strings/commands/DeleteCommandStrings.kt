package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.DeleteCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import java.io.File

object DeleteCommandStrings {
    private const val FILE_NAME = "delete-command.json"
    private lateinit var data: DeleteCommandStringData

    val DESCRIPTION: String get() = data.DESCRIPTION
    val USAGE_DELETE: String get() = data.USAGE_DELETE
    val USAGE_DELETE_NAME: String get() = data.USAGE_DELETE_NAME
    val DELETE_DEFAULT_HOME: String get() = data.DELETE_DEFAULT_HOME
    val DELETE_NAMED_HOME: String get() = data.DELETE_NAMED_HOME

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { DeleteCommandStringData() }
    }
}
