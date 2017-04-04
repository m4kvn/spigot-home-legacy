package com.masahirosaito.spigot.homes.commands.subcommands.listcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission.home_command_list
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.HOME_LIST
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.PLAYER_LIST
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.USAGE_CONSOLE_COMMAND_LIST
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.USAGE_LIST_PLAYER
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.USAGE_PLAYER_COMMAND_LIST
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class ListCommand(override val homes: Homes) : PlayerCommand, ConsoleCommand {
    override val name: String = "list"
    override val description: String = DESCRIPTION()
    override val permissions: List<String> = listOf(home_command_list)
    override val consoleCommandUsage: CommandUsage = CommandUsage(this, listOf(
            "/home list" to USAGE_CONSOLE_COMMAND_LIST(),
            "/home list <player_name>" to USAGE_LIST_PLAYER()
    ))
    override val playerCommandUsage: CommandUsage = CommandUsage(this, listOf(
            "/home list" to USAGE_PLAYER_COMMAND_LIST(),
            "/home list <player_name>" to USAGE_LIST_PLAYER()
    ))
    override val commands: List<BaseCommand> = listOf(
            ListPlayerCommand(this)
    )

    override fun fee(): Double = homes.fee.LIST

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        listHome(player)
    }

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        send(consoleCommandSender, PLAYER_LIST())
    }

    private fun listHome(player: Player) {
        send(player, HOME_LIST(PlayerDataManager.findPlayerData(player), false))
    }
}
