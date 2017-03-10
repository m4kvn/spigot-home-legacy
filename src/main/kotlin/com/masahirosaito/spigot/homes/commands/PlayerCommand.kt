package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
import org.bukkit.entity.Player

interface PlayerCommand : BaseCommand {
    val permissions: List<String>
    val fee: Double

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
        plugin.econ?.let { economy ->
            val r = economy.depositPlayer(player, fee)
            if (r.transactionSuccess()) {
                send(player, buildString {
                    append("You were given ${economy.format(r.amount)}")
                    append(" and now have ${economy.format(r.balance)}")
                })
            } else {
                throw Exception("An error occurred: ${r.errorMessage}")
            }
        }
    }

    private fun checkBalance(player: Player) {
        plugin.econ?.let {
            if (it.getBalance(player) - fee < 0)
                throw Exception("You have not enough money to execute this command (fee: $fee)")
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
