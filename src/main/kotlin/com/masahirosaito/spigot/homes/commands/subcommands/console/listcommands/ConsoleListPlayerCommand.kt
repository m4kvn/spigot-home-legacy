package com.masahirosaito.spigot.homes.commands.subcommands.console.listcommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.createHomeListMessage
import org.bukkit.command.ConsoleCommandSender

class ConsoleListPlayerCommand(consoleListCommand: ConsoleListCommand) :
        SubCommand(consoleListCommand), ConsoleCommand {

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        send(
            consoleCommandSender,
            createHomeListMessage(PlayerDataManager.findPlayerData(findOfflinePlayer(args[0])), false)
        )
    }

    override fun configs(): List<Boolean> = listOf(onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1
}
