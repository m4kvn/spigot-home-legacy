package com.masahirosaito.spigot.homes.commands.subcommands.deletecommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.findPlayerHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class DeleteCommand(override val plugin: Homes) : PlayerCommand {
    override val name: String = "delete"
    override val description: String = "Delete your homes"
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_delete
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home delete" to "Delete your default home",
            "/home delete <home_name>" to "Delete your named home"
    ))
    override val commands: List<BaseCommand> = listOf(
            DeleteNameCommand(this)
    )

    override fun fee(): Double = plugin.fee.DELETE

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        player.findPlayerHome(plugin).removeDefaultHome(player)
        send(player, "${ChatColor.AQUA}Successfully delete your default home${ChatColor.RESET}")
    }
}
