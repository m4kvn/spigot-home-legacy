package com.masahirosaito.spigot.homes.nms

interface NMSEntityArmorStand {
    fun dead()
    fun setNMSName(name: String)
    fun setNameVisible(boolean: Boolean)
    fun setPosition(x: Double, y: Double, z: Double)
}
