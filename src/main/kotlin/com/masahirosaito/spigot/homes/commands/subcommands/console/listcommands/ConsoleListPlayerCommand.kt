package com.masahirosaito.spigot.homes.commands.subcommands.console.listcommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.HOME_LIST
import org.bukkit.command.ConsoleCommandSender

class ConsoleListPlayerCommand(consoleListCommand: ConsoleListCommand) :
        SubCommand(consoleListCommand), ConsoleCommand {

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        PlayerDataManager.findPlayerData(findOfflinePlayer(args[0])).let {
            send(consoleCommandSender, HOME_LIST(it, false))
        }
    }

    override fun configs(): List<Boolean> = listOf(onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1
}
