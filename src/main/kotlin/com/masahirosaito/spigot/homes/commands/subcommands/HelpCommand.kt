package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.homes.exceptions.CommandArgumentIncorrectException
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class HelpCommand(override val plugin: Homes, val homeCommand: HomeCommand) : SubCommand {
    override val name: String = "help"
    override val permission: String = Permission.home_command_help
    override var resultMessage: String = ""
    override val description: String = "Homes Help Command"
    override val usage: String = buildString {
        append("${ChatColor.GOLD}Help Command Usage:\n")
        append("${ChatColor.AQUA}/home help${ChatColor.RESET} : Display the list of Homes commands\n")
        append("${ChatColor.AQUA}/home help <command_name>${ChatColor.RESET} : Display the usage of Homes command")
    }

    override fun execute(player: Player, args: List<String>) {

        when (args.size) {
            0 -> showCommands()
            1 -> showCommandUsage(player, args[0])
            else -> throw CommandArgumentIncorrectException(this)
        }
    }

    private fun showCommands() {
        resultMessage = buildString {
            append("${ChatColor.GOLD}Homes command list${ChatColor.RESET}\n")
            append(ChatColor.LIGHT_PURPLE)
            append("/home help <command_name> : Display the usage of command\n")
            append(ChatColor.RESET)
            homeCommand.subCommands.forEach {
                append("  ${ChatColor.AQUA}${it.name}${ChatColor.RESET} : ${it.description}\n")
            }
        }
    }

    private fun showCommandUsage(player: Player, name: String) {

        if (!player.hasPermission(Permission.home_command_help_command))
            throw NotHavePermissionException(Permission.home_command_help_command)

        val subCommand = homeCommand.subCommands.find { it.name == name }
                ?: throw NoSuchCommandException(name)

        resultMessage = subCommand.usage
    }
}
