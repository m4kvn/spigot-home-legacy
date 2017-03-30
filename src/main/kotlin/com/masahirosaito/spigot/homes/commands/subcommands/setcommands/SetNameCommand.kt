package com.masahirosaito.spigot.homes.commands.subcommands.setcommands

import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetNameCommand(setCommand: SetCommand) : SubCommand(setCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_set_name
    )

    override fun configs(): List<Boolean> = listOf(
            plugin.configs.onNamedHome
    )

    override fun fee(): Double = plugin.fee.SET_NAME

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        plugin.playerDataManager.setNamedHome(player, player.location, args[0])
        send(player, "${ChatColor.AQUA}Successfully set as ${ChatColor.GOLD}home named" +
                " <${ChatColor.RESET}${args[0]}${ChatColor.GOLD}>${ChatColor.RESET}")
    }
}
