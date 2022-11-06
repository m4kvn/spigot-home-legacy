package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.datas.strings.TeleportStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.strings.Strings.DELAY
import java.io.File

object TeleportStrings {
    private const val FILE_NAME = "teleport.json"
    private lateinit var teleport: TeleportStringData

    val TELEPORT_CANCEL: String get() = teleport.TELEPORT_CANCEL

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        teleport = loadDataAndSave(file) { TeleportStringData() }
    }

    fun createTeleportWait(delay: Int) =
        teleport.TELEPORT_WAIT
            .replace(DELAY, delay.toString())

    fun createTeleportCancelDelay(delay: Int) =
        teleport.TELEPORT_CANCEL_DELAY
            .replace(DELAY, delay.toString())
}
