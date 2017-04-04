package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Permission.home_admin
import com.masahirosaito.spigot.homes.exceptions.HomesException
import com.masahirosaito.spigot.homes.exceptions.NoPermissionException
import org.bukkit.entity.Player

interface PlayerCommand : BaseCommand {
    val permissions: List<String>

    fun fee(): Double

    fun execute(player: Player, args: List<String>)

    fun onCommand(player: Player, args: List<String>) {
        checkConfig()
        checkPermission(player)
        checkArgs(args)
        checkBalance(player)
        execute(player, args)
        payFee(player)
    }

    fun payFee(player: Player) {
        if (fee() <= 0) return
        homes.econ?.let { economy ->
            val r = economy.withdrawPlayer(player, fee())
            if (r.transactionSuccess()) {
                send(player, buildString {
                    append("You paid ${economy.format(r.amount)}")
                    append(" and now have ${economy.format(r.balance)}")
                })
            } else {
                throw HomesException("An error occurred: ${r.errorMessage}")
            }
        }
    }

    fun checkBalance(player: Player) {
        if (fee() <= 0) return
        homes.econ?.let {
            if (!it.hasAccount(player)) {
                throw HomesException("You are not registered as Economy")
            }
            if (!it.has(player, fee())) {
                throw HomesException("You have not enough money to execute this command (fee: ${fee()})")
            }
        }
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
