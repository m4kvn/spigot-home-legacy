package com.masahirosaito.spigot.homes.commands.subcommands.player.deletecommands

import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.Permission.home_command_delete
import com.masahirosaito.spigot.homes.Permission.home_command_delete_name
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_NAMED_HOME
import org.bukkit.entity.Player

class DeleteNameCommand(deleteCommand: DeleteCommand) : SubCommand(deleteCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            home_command_delete,
            home_command_delete_name
    )

    override fun fee(): Double = homes.fee.DELETE_NAME

    override fun configs(): List<Boolean> = listOf(onNamedHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        PlayerDataManager.removeNamedHome(player, args[0])
        send(player, DELETE_NAMED_HOME(args[0]))
    }
}
