package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.datas.strings.commands.PrivateCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object PrivateCommandStrings {
    lateinit private var data: PrivateCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "private-command.json").load(), PrivateCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_PRIVATE() =
            data.USAGE_PRIVATE

    fun USAGE_PRIVATE_NAME() =
            data.USAGE_PRIVATE_NAME

    fun SET_DEFAULT_HOME_PRIVATE() =
            data.SET_DEFAULT_HOME_PRIVATE

    fun SET_DEFAULT_HOME_PUBLIC() =
            data.SET_DEFAULT_HOME_PUBLIC

    fun SET_NAMED_HOME_PRIVATE(homeName: String) =
            data.SET_NAMED_HOME_PRIVATE
                    .replace(HOME_NAME, homeName)

    fun SET_NAMED_HOME_PUBLIC(homeName: String) =
            data.SET_NAMED_HOME_PUBLIC
                    .replace(HOME_NAME, homeName)
}
