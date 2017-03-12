package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.*
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class HelpCommand(val mainCommand: MainCommand) : PlayerCommand {
    override val plugin: Homes = mainCommand.plugin
    override val name: String = "help"
    override val description: String = "Homes Help Command"
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_help
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home help" to "Display the list of Homes commands",
            "/home help <command_name>" to "Display the usage of Homes command"
    ))
    override val commands: List<BaseCommand> = listOf(
            HelpUsageCommand(this)
    )

    override fun fee(): Double = plugin.fee.HELP

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        send(player, msg())
    }

    fun msg() = buildString {
        append("${ChatColor.GOLD}Homes command list${ChatColor.RESET}\n")
        append(ChatColor.LIGHT_PURPLE)
        append("/home help <command_name> : Display the usage of command\n")
        append(ChatColor.RESET)
        mainCommand.subCommands.forEach {
            append("  ${ChatColor.AQUA}${it.name}${ChatColor.RESET} : ${it.description}\n")
        }
    }

    class HelpUsageCommand(val helpCommand: HelpCommand) : SubCommand(helpCommand), PlayerCommand {
        override val permissions: List<String> = listOf(
                Permission.home_command,
                Permission.home_command_help_command
        )

        override fun fee(): Double = plugin.fee.HELP_USAGE

        override fun configs(): List<Boolean> = listOf()

        override fun isValidArgs(args: List<String>): Boolean = args.size == 1

        override fun execute(player: Player, args: List<String>) {
            helpCommand.mainCommand.subCommands.find { it.name == args[0] }?.let {
                send(player, it.usage)
            } ?: throw NoSuchCommandException(args[0])
        }
    }
}
