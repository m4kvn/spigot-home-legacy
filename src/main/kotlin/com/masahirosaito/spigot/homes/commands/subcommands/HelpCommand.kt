package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
import com.masahirosaito.spigot.homes.findDefaultHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class HelpCommand(override val plugin: Homes) : PlayerCommand {
    override val name: String = "help"
    override val fee: Double = plugin.fee.HELP
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

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {

    }

    class HelpUsageCommand(helpCommand: HelpCommand) : SubCommand(helpCommand), PlayerCommand {
        override val fee: Double = plugin.fee.HELP_USAGE
        override val permissions: List<String> = listOf(
                Permission.home_command,
                Permission.home_command_help_command
        )

        override fun configs(): List<Boolean> = listOf()

        override fun isValidArgs(args: List<String>): Boolean = args.size == 1

        override fun execute(player: Player, args: List<String>) {

        }
    }

//    override fun isInValidArgs(args: List<String>): Boolean = args.size > 1
//
//    override fun execute(player: Player, args: List<String>) {
//        when (args.size) {
//            0 -> showCommands(player)
//            1 -> showCommandUsage(player, args[0])
//        }
//    }
//
//    private fun showCommands(player: Player) {
//        send(player, buildString {
//            append("${ChatColor.GOLD}Homes command list${ChatColor.RESET}\n")
//            append(ChatColor.LIGHT_PURPLE)
//            append("/home help <command_name> : Display the usage of command\n")
//            append(ChatColor.RESET)
//            homeCommand.subCommands.forEach {
//                append("  ${ChatColor.AQUA}${it.name()}${ChatColor.RESET} : ${it.description()}\n")
//            }
//        })
//    }
//
//    private fun showCommandUsage(player: Player, name: String) {
//        send(player, homeCommand.findSubCommand(name).usage())
//    }
}
