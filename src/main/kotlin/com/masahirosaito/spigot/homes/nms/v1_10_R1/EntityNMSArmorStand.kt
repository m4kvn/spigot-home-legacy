package com.masahirosaito.spigot.homes.nms.v1_10_R1

import net.minecraft.server.v1_10_R1.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld

class EntityNMSArmorStand(world: World) : EntityArmorStand(world) {

    constructor(loc: Location, displayName: String) : this((loc.world as CraftWorld).handle) {
        setArms(false)
        setBasePlate(true)
        locX = loc.x
        locY = loc.y + 0.8
        locZ = loc.z
        customName = displayName
        customNameVisible = true
        isNoGravity = true
        isMarker = true
        isSmall = true
        collides = false
        isInvisible = true
    }

    override fun a(nbttagcompound: NBTTagCompound?) {}
    override fun b(nbttagcompound: NBTTagCompound?) {}
    override fun c(nbttagcompound: NBTTagCompound?): Boolean = false
    override fun d(nbttagcompound: NBTTagCompound?): Boolean = false
    override fun e(nbttagcompound: NBTTagCompound?): NBTTagCompound = nbttagcompound!!
    override fun f(nbttagcompound: NBTTagCompound?) {}
    override fun isInvulnerable(damagesource: DamageSource?): Boolean = true
    override fun isCollidable(): Boolean = false
    override fun setCustomName(s: String?) {}
    override fun setCustomNameVisible(flag: Boolean) {}
    override fun a(axisalignedbb: AxisAlignedBB?) {}
    override fun c(i: Int, itemstack: ItemStack?): Boolean = false
    override fun setSlot(enumitemslot: EnumItemSlot?, itemstack: ItemStack?) {}
    override fun inactiveTick() {}
    override fun a(soundeffect: SoundEffect?, f: Float, f1: Float) {}
    override fun die() {}
//
//    override fun getBukkitEntity(): CraftEntity {
//        return super.bukkitEntity ?: CraftNMSArmorStand(super.world.server, this).apply { super.bukkitEntity = this }
//    }
//
//    override fun getId(): Int {
//        val element: StackTraceElement = Throwable::class.java
//                .getDeclaredMethod("getStackTraceElement", Int::class.java).apply { isAccessible = true }
//                .invoke(Throwable(), 2) as StackTraceElement
//        if (element.fileName == "EntityTrackerEntry.java" && element.lineNumber in 159..167) return -1
//        return super.getId()
//    }

//    fun setCustomNameNMS(name: String) {
//        if (name.isNullOrBlank()) return
//        super.setCustomName(name.substring(0, 300))
//        super.setCustomNameVisible(true)
//    }
//
//    fun getCustomnameNMS(): String = super.getCustomName()
//
//    fun killEntityNMS() { super.dead = true }
//
//    fun setLocationNMS(x: Double, y: Double, z: Double) {
//        super.setPosition(x, y, z)
//
//        val teleportPacket = PacketPlayOutEntityTeleport(this)
//
//        super.world.players.forEach {
//            if (it is EntityPlayer) {
//                val dis = square(it.locX - super.locX) + square(it.locZ - super.locZ)
//                if (dis < 8192 && it.playerConnection != null) {
//                    it.playerConnection.sendPacket(teleportPacket)
//                }
//            }
//        }
//    }
//
//    fun isDeadNMS(): Boolean = super.dead
//
//    fun getIdNMS(): Int = super.getId()
//
//    fun forceSetBoundingBox(boundingBox: AxisAlignedBB) {
//        super.a(boundingBox)
//    }
//
//    private fun square(num: Double) = num * num
}
