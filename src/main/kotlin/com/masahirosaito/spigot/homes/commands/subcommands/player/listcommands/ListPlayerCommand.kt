package com.masahirosaito.spigot.homes.commands.subcommands.player.listcommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.Permission.home_admin
import com.masahirosaito.spigot.homes.Permission.home_command_list
import com.masahirosaito.spigot.homes.Permission.home_command_list_player
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.HOME_LIST
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class ListPlayerCommand(val listCommand: ListCommand) : SubCommand(listCommand), PlayerCommand, ConsoleCommand {
    override val permissions: List<String> = listOf(home_command_list, home_command_list_player)

    override fun fee(): Double = homes.fee.LIST_PLAYER

    override fun configs(): List<Boolean> = listOf(onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        PlayerDataManager.findPlayerData(findOfflinePlayer(args[0])).let {
            send(player, HOME_LIST(it, !player.hasPermission(home_admin)))
        }
    }

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        PlayerDataManager.findPlayerData(findOfflinePlayer(args[0])).let {
            send(consoleCommandSender, HOME_LIST(it, false))
        }
    }
}
