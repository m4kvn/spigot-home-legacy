package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.datas.strings.TeleportStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import com.masahirosaito.spigot.homes.strings.Strings.DELAY
import java.io.File

object TeleportStrings {
    lateinit private var teleport: TeleportStringData

    fun load(folderPath: String) {
        teleport = loadData(File(folderPath, "teleport.json").load(), TeleportStringData::class.java)
    }

    fun TELEPORT_WAIT(delay: Int) =
            teleport.TELEPORT_WAIT
                    .replace(DELAY, delay.toString())

    fun TELEPORT_CANCEL() =
            teleport.TELEPORT_CANCEL

    fun TELEPORT_CANCEL_DELAY(delay: Int) =
            teleport.TELEPORT_CANCEL_DELAY
                    .replace(DELAY, delay.toString())
}
