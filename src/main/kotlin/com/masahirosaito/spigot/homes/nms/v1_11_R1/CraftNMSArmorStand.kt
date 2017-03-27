package com.masahirosaito.spigot.homes.nms.v1_11_R1

import org.bukkit.EntityEffect
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_11_R1.CraftServer
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftArmorStand
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

class CraftNMSArmorStand(server: CraftServer, entity: EntityNMSArmorStand) : CraftArmorStand(server, entity) {

    override fun remove() {}

    override fun setArms(arms: Boolean) {}
    override fun setBasePlate(basePlate: Boolean) {}
    override fun setBodyPose(pose: EulerAngle?) {}
    override fun setBoots(item: ItemStack?) {}
    override fun setChestplate(item: ItemStack?) {}
    override fun setHeadPose(pose: EulerAngle?) {}
    override fun setHelmet(item: ItemStack?) {}
    override fun setItemInHand(item: ItemStack?) {}
    override fun setLeftArmPose(pose: EulerAngle?) {}
    override fun setLeftLegPose(pose: EulerAngle?) {}
    override fun setLeggings(item: ItemStack?) {}
    override fun setRightArmPose(pose: EulerAngle?) {}
    override fun setRightLegPose(pose: EulerAngle?) {}
    override fun setSmall(small: Boolean) {}
    override fun setVisible(visible: Boolean) {}
    override fun setMarker(marker: Boolean) {}

    override fun addPotionEffect(effect: PotionEffect?): Boolean = false
    override fun addPotionEffect(effect: PotionEffect?, force: Boolean): Boolean = false
    override fun addPotionEffects(effects: MutableCollection<PotionEffect>?): Boolean = false
    override fun setRemoveWhenFarAway(remove: Boolean) {}
    override fun setAI(ai: Boolean) {}
    override fun setCanPickupItems(pickup: Boolean) {}
    override fun setCollidable(collidable: Boolean) {}
    override fun setGliding(gliding: Boolean) {}
    override fun setLeashHolder(holder: Entity?): Boolean = false

    override fun setVelocity(vel: Vector?) {}
    override fun teleport(destination: Entity?): Boolean = false
    override fun teleport(location: Location?): Boolean = false
    override fun teleport(destination: Entity?, cause: PlayerTeleportEvent.TeleportCause?): Boolean = false
    override fun teleport(location: Location?, cause: PlayerTeleportEvent.TeleportCause?): Boolean = false
    override fun setFireTicks(ticks: Int) {}
    override fun setPassenger(passenger: Entity?): Boolean = false
    override fun eject(): Boolean = false
    override fun leaveVehicle(): Boolean = false
    override fun playEffect(type: EntityEffect?) {}
    override fun setCustomName(name: String?) {}
    override fun setCustomNameVisible(flag: Boolean) {}
    override fun setGlowing(flag: Boolean) {}
    override fun setGravity(gravity: Boolean) {}
    override fun setInvulnerable(flag: Boolean) {}
    override fun setMomentum(value: Vector?) {}
    override fun setSilent(flag: Boolean) {}
    override fun setTicksLived(value: Int) {}
}
