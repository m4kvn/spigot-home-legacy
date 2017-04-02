package com.masahirosaito.spigot.homes.nms

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.nms.v1_10_R1.NMSController_v1_10_R1
import com.masahirosaito.spigot.homes.nms.v1_11_R1.NMSController_v1_11_R1
import org.bukkit.Location
import org.bukkit.OfflinePlayer

interface NMSController {

    fun setUp()

    fun spawn(homesEntity: HomesEntity): List<NMSEntityArmorStand>

    companion object {
        fun load(homes: Homes): NMSController {
            return when (homes.server.bukkitVersion) {
                "1.10-R0.1-SNAPSHOT", "1.10.2-R0.1-SNAPSHOT" -> NMSController_v1_10_R1()
                "1.11-R0.1-SNAPSHOT", "1.11.2-R0.1-SNAPSHOT" -> NMSController_v1_11_R1()
                else -> DummyNMSController()
            }
        }
    }
}
