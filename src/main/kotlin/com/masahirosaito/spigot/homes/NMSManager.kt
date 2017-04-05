package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.nms.NMSController
import com.masahirosaito.spigot.homes.nms.NMSEntityArmorStand

object NMSManager {
    lateinit private var nmsController: NMSController

    fun load() {
        nmsController = NMSController.load()
        nmsController.setUp()
    }

    fun spawn(homesEntity: HomesEntity): List<NMSEntityArmorStand> {
        return nmsController.spawn(homesEntity)
    }
}
