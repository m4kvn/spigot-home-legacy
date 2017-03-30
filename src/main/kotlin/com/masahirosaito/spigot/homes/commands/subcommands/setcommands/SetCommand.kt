package com.masahirosaito.spigot.homes.commands.subcommands.setcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetCommand(override val plugin: Homes) : PlayerCommand {
    override val name: String = "set"
    override val description: String = "Set your home or named home"
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_set
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home set" to "Set your location to your default home",
            "/home set <home_name>" to "Set your location to your named home"
    ))
    override val commands: List<BaseCommand> = listOf(
            SetNameCommand(this)
    )

    override fun fee(): Double = plugin.fee.SET

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        plugin.playerDataManager.setDefaultHome(player, player.location)
        send(player, "${ChatColor.AQUA}Successfully set as ${ChatColor.GOLD}default home${ChatColor.RESET}")
    }
}
