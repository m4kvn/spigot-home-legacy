package com.masahirosaito.spigot.homes.nms.v1_11_R1

import com.masahirosaito.spigot.homes.nms.NMSManager
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld
import org.bukkit.event.entity.CreatureSpawnEvent

class NMSManager_v1_11_R1 : NMSManager {

    override fun setUp() {
        CustomEntities.registerEntities()
    }

    override fun spawnNMSArmorStand(loc: Location, name: String) {
        (loc.world as CraftWorld).handle
                .addEntity(EntityNMSArmorStand(loc, name), CreatureSpawnEvent.SpawnReason.CUSTOM)
    }
}
