package com.masahirosaito.spigot.homes.nms

class DummyNMSController : NMSController {

    override fun setUp() {
    }

    override fun spawn(homesEntity: HomesEntity): List<NMSEntityArmorStand> {
        return emptyList()
    }
}
