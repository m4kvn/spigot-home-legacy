package com.masahirosaito.spigot.homes.nms.v1_11_R1

//class NMSController_v1_11_R1 : NMSController {
//
//    override fun setUp() {
//        CustomEntities.registerEntities()
//    }
//
//    override fun spawn(homesEntity: HomesEntity): List<NMSEntityArmorStand> {
//        val texts = if (homesEntity.homeName == null) {
//            createDefaultHomeDisplayName(homesEntity.offlinePlayer.name).split("\n")
//        } else {
//            createNamedHomeDisplayName(homesEntity.offlinePlayer.name, homesEntity.homeName!!).split("\n")
//        }
//        val list: MutableList<NMSEntityArmorStand> = mutableListOf()
//        val location = homesEntity.location
//
//        texts.forEachIndexed { index, text ->
//            list.add(EntityNMSArmorStand((location.world as CraftWorld).handle).apply {
//                setNMSName(text)
//                setNameVisible(!homesEntity.isPrivate)
//                setPosition(location.x, location.y + 0.8 - (index * 0.2), location.z)
//                (homesEntity.location.world as CraftWorld).handle
//                        .addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM)
//            })
//        }
//        return list
//    }
//}
