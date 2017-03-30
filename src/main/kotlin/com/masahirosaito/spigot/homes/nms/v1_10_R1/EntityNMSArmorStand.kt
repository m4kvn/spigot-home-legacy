package com.masahirosaito.spigot.homes.nms.v1_10_R1

import com.masahirosaito.spigot.homes.nms.NMSEntityArmorStand
import net.minecraft.server.v1_10_R1.*

class EntityNMSArmorStand(world: World) : EntityArmorStand(world), NMSEntityArmorStand {

    init {
        super.setInvisible(true)
        super.setArms(false)
        super.setBasePlate(true)
        super.setNoGravity(true)
        super.setMarker(true)
        super.setSmall(true)
        super.collides = false
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

    override fun dead() {
        super.dead = true
    }

    override fun setNMSName(name: String) {
        customName = name
    }

    override fun setNameVisible(boolean: Boolean) {
        customNameVisible = boolean
    }

    override fun setPosition(x: Double, y: Double, z: Double) {
        super.setPosition(x, y, z)
    }
}
