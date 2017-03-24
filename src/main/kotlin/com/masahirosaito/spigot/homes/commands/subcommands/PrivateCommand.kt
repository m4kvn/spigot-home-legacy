package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.findDefaultHome
import com.masahirosaito.spigot.homes.findNamedHome
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
            player.findDefaultHome(plugin).isPrivate = true
            send(player, "Set your default home ${ChatColor.YELLOW}PRIVATE${ChatColor.RESET}")
        } else {
            player.findDefaultHome(plugin).isPrivate = false
            send(player, "Set your default home ${ChatColor.AQUA}PUBLIC${ChatColor.RESET}")
        }
    }

    class PrivateNameCommand(privateCommand: PrivateCommand) : SubCommand(privateCommand), PlayerCommand {
        override val permissions: List<String> = listOf(
                Permission.home_command,
                Permission.home_command_private_name
        )

        override fun fee(): Double = plugin.fee.PRIVATE_NAME

        override fun configs(): List<Boolean> = listOf(
                plugin.configs.onPrivate,
                plugin.configs.onNamedHome
        )

        override fun isValidArgs(args: List<String>): Boolean = args.size == 2 && (args[0] == "on" || args[0] == "off")

        override fun execute(player: Player, args: List<String>) {
            if (args[0] == "on") {
                player.findNamedHome(plugin, args[1]).isPrivate = true
                send(player, "Set your home named <${ChatColor.LIGHT_PURPLE}${args[1]}${ChatColor.RESET}>" +
                        " ${ChatColor.YELLOW}PRIVATE${ChatColor.RESET}")
            } else {
                player.findNamedHome(plugin, args[1]).isPrivate = false
                send(player, "Set your home named <${ChatColor.LIGHT_PURPLE}${args[1]}${ChatColor.RESET}>" +
                        " ${ChatColor.AQUA}PUBLIC${ChatColor.RESET}")
            }
        }
    }
}
