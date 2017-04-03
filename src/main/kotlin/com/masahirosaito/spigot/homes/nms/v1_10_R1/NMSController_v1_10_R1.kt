package com.masahirosaito.spigot.homes.nms.v1_10_R1

import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.nms.NMSController
import com.masahirosaito.spigot.homes.nms.NMSEntityArmorStand
import com.masahirosaito.spigot.homes.strings.HomeDisplayStrings.DEFAULT_HOME_DISPLAY_NAME
import com.masahirosaito.spigot.homes.strings.HomeDisplayStrings.NAMED_HOME_DISPLAY_NAME
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld
import org.bukkit.event.entity.CreatureSpawnEvent

class NMSController_v1_10_R1 : NMSController {

    override fun setUp() {
        CustomEntities.registerEntities()
    }

    override fun spawn(homesEntity: HomesEntity): List<NMSEntityArmorStand> {
        val texts = if (homesEntity.homeName == null) {
            DEFAULT_HOME_DISPLAY_NAME(homesEntity.offlinePlayer.name).split("\n")
        } else {
            NAMED_HOME_DISPLAY_NAME(homesEntity.offlinePlayer.name, homesEntity.homeName!!).split("\n")
        }
        val list: MutableList<NMSEntityArmorStand> = mutableListOf()
        val location = homesEntity.location

        texts.forEachIndexed { index, text ->
            list.add(EntityNMSArmorStand((location.world as CraftWorld).handle).apply {
                setNMSName(text)
                setNameVisible(!homesEntity.isPrivate)
                setPosition(location.x, location.y + 0.8 - (index * 0.2), location.z)
                (homesEntity.location.world as CraftWorld).handle
                        .addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM)
            })
        }
        return list
    }
}
