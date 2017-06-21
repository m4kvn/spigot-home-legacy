package com.masahirosaito.spigot.homes.commands.subcommands.player

import com.masahirosaito.spigot.homes.PayController
import com.masahirosaito.spigot.homes.Permission.home_admin
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.exceptions.NoPermissionException
import org.bukkit.entity.Player

interface PlayerCommand : BaseCommand {
    val permissions: List<String>
    var payNow: Boolean

    fun fee(): Double

    fun execute(player: Player, args: List<String>)

    fun onCommand(player: Player, args: List<String>) {
        checkConfig()
        checkPermission(player)
        checkArgs(args)
        PayController.checkBalance(this, player)
        execute(player, args)
        if (payNow) PayController.payFee(this, player)
    }

    fun checkPermission(player: Player) {
        if (player.hasPermission(home_admin)) return
        if (!permissions.isEmpty()) {
            permissions.forEach {
                if (!player.hasPermission(it)) throw NoPermissionException(it)
            }
        }
    }

    fun hasPermission(player: Player): Boolean {
        if (player.hasPermission(home_admin)) return true
        permissions.forEach { if (!player.hasPermission(it)) return false }
        return true
    }
}
