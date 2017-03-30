package com.masahirosaito.spigot.homes.commands.subcommands.privatecommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PrivateCommand(override val plugin: Homes) : PlayerCommand {
    override val name: String = "private"
    override val description: String = "Set your home private or public"
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_private
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home private (on/off)" to "Set your default home private or public",
            "/home private (on/off) <home_name>" to "Set your named home private or public"
    ))
    override val commands: List<BaseCommand> = listOf(
            PrivateNameCommand(this)
    )

    override fun fee(): Double = plugin.fee.PRIVATE

    override fun configs(): List<Boolean> = listOf(
            plugin.configs.onPrivate
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1 && (args[0] == "on" || args[0] == "off")

    override fun execute(player: Player, args: List<String>) {
        if (args[0] == "on") {
            plugin.playerDataManager.findDefaultHome(player).changePrivate(true)
            send(player, "Set your default home ${ChatColor.YELLOW}PRIVATE${ChatColor.RESET}")
        } else {
            plugin.playerDataManager.findDefaultHome(player).changePrivate(false)
            send(player, "Set your default home ${ChatColor.AQUA}PUBLIC${ChatColor.RESET}")
        }
    }
}
