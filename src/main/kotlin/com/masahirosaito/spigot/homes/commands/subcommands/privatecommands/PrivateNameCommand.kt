package com.masahirosaito.spigot.homes.commands.subcommands.privatecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PrivateNameCommand(privateCommand: PrivateCommand) : SubCommand(privateCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_private_name
    )

    override fun fee(): Double = plugin.fee.PRIVATE_NAME

    override fun configs(): List<Boolean> = listOf(
            Configs.onPrivate,
            Configs.onNamedHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2 && (args[0] == "on" || args[0] == "off")

    override fun execute(player: Player, args: List<String>) {
        if (args[0] == "on") {
            PlayerDataManager.setNamedHomePrivate(player, args[1], true)
            send(player, "Set your home named <${ChatColor.LIGHT_PURPLE}${args[1]}${ChatColor.RESET}>" +
                    " ${ChatColor.YELLOW}PRIVATE${ChatColor.RESET}")
        } else {
            PlayerDataManager.setNamedHomePrivate(player, args[1], false)
            send(player, "Set your home named <${ChatColor.LIGHT_PURPLE}${args[1]}${ChatColor.RESET}>" +
                    " ${ChatColor.AQUA}PUBLIC${ChatColor.RESET}")
        }
    }
}
