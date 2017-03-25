package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.exceptions.HomesException
import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
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

    private fun payFee(player: Player) {
        if (fee() <= 0) return
        plugin.econ?.let { economy ->
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

    private fun checkBalance(player: Player) {
        if (fee() <= 0) return
        plugin.econ?.let {
            if (!it.hasAccount(player)) {
                throw HomesException("You are not registered as Economy")
            }
            if (!it.has(player, fee())) {
                throw HomesException("You have not enough money to execute this command (fee: ${fee()})")
            }
        }
    }

    private fun checkPermission(player: Player) {
        if (!permissions.isEmpty()) {
            permissions.forEach {
                if (!player.hasPermission(it)) throw NotHavePermissionException(it)
            }
        }
    }
}