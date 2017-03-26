package com.masahirosaito.spigot.homes.commands.subcommands.listcommands

import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.findPlayerHome
import org.bukkit.entity.Player

class ListPlayerCommand(val listCommand: ListCommand) : SubCommand(listCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_list_player
    )

    override fun fee(): Double = plugin.fee.LIST_PLAYER

    override fun configs(): List<Boolean> = listOf(
            plugin.configs.onFriendHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        listPlayerHome(player, args)
    }

    private fun listPlayerHome(player: Player, args: List<String>) {
        send(player, listCommand.getResultMessage(findOfflinePlayer(args[0]).findPlayerHome(plugin), true))
    }
}
