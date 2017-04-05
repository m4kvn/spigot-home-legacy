package com.masahirosaito.spigot.homes.commands.subcommands.player

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_admin
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.exceptions.HomesException
import com.masahirosaito.spigot.homes.exceptions.NoPermissionException
import com.masahirosaito.spigot.homes.strings.EconomyStrings.ECONOMY_ERROR
import com.masahirosaito.spigot.homes.strings.EconomyStrings.NOT_ENOUGH_MONEY_ERROR
import com.masahirosaito.spigot.homes.strings.EconomyStrings.NO_ACCOUNT_ERROR
import com.masahirosaito.spigot.homes.strings.EconomyStrings.PAY
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
                send(player, PAY(economy.format(r.amount), economy.format(r.balance)))
            } else {
                throw HomesException(ECONOMY_ERROR(r.errorMessage))
            }
        }
    }

    fun checkBalance(player: Player) {
        if (fee() <= 0) return
        homes.econ?.let {
            if (!it.hasAccount(player)) {
                throw HomesException(NO_ACCOUNT_ERROR())
            }
            if (!it.has(player, fee())) {
                throw HomesException(NOT_ENOUGH_MONEY_ERROR(fee()))
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
