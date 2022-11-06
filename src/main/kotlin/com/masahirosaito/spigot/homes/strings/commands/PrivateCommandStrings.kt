package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.PrivateCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import java.io.File

object PrivateCommandStrings {
    private const val FILE_NAME = "private-command.json"
    private lateinit var data: PrivateCommandStringData

    val DESCRIPTION: String get() = data.DESCRIPTION
    val USAGE_PRIVATE: String get() = data.USAGE_PRIVATE
    val USAGE_PRIVATE_NAME: String get() = data.USAGE_PRIVATE_NAME
    val SET_DEFAULT_HOME_PRIVATE: String get() = data.SET_DEFAULT_HOME_PRIVATE
    val SET_DEFAULT_HOME_PUBLIC: String get() = data.SET_DEFAULT_HOME_PUBLIC

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { PrivateCommandStringData() }
    }

    fun createSetNamedHomePrivateMessage(homeName: String) =
        data.SET_NAMED_HOME_PRIVATE
            .replace(HOME_NAME, homeName)

    fun createSetNamedHomePublicMessage(homeName: String) =
        data.SET_NAMED_HOME_PUBLIC
            .replace(HOME_NAME, homeName)
}
