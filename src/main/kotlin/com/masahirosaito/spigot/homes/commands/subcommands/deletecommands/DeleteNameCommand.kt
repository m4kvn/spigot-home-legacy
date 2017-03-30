package com.masahirosaito.spigot.homes.commands.subcommands.deletecommands

import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.Strings
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.entity.Player

class DeleteNameCommand(deleteCommand: DeleteCommand) : SubCommand(deleteCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_delete_name
    )

    override fun fee(): Double = plugin.fee.DELETE_NAME

    override fun configs(): List<Boolean> = listOf(
            plugin.configs.onNamedHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        plugin.playerDataManager.removeNamedHome(player, args[0])
        send(player, Strings.REMOVE_NAMED_HOME(args[0]))
    }
}
