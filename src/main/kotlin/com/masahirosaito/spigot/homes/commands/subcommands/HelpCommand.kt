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

    override fun name(): String = "help"

    override fun permission(): String = Permission.home_command_help

    override fun description(): String = "Homes Help Command"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home help" to "Display the list of Homes commands",
            "/home help <command_name>" to "Display the usage of Homes command"
    )

    override fun configs(): List<Boolean> = listOf()

    override fun isInValidArgs(args: List<String>): Boolean = args.size > 1

    override fun execute(player: Player, args: List<String>) {
        when (args.size) {
            0 -> showCommands(player)
            1 -> showCommandUsage(player, args[0])
        }
    }

    private fun showCommands(player: Player) {
        send(player, buildString {
            append("${ChatColor.GOLD}Homes command list${ChatColor.RESET}\n")
            append(ChatColor.LIGHT_PURPLE)
            append("/home help <command_name> : Display the usage of command\n")
            append(ChatColor.RESET)
            homeCommand.subCommands.forEach {
                append("  ${ChatColor.AQUA}${it.name()}${ChatColor.RESET} : ${it.description()}\n")
            }
        })
    }

    private fun showCommandUsage(player: Player, name: String) {
        checkPermission(player, Permission.home_command_help_command)
        send(player, homeCommand.findSubCommand(name).usage())
    }
}
