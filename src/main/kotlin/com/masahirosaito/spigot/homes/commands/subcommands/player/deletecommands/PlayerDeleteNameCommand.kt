package com.masahirosaito.spigot.homes.commands.subcommands.player.deletecommands

import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_delete
import com.masahirosaito.spigot.homes.Permission.home_command_delete_name
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerSubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_NAMED_HOME
import org.bukkit.entity.Player

class PlayerDeleteNameCommand(playerDeleteCommand: PlayerDeleteCommand) :
        PlayerSubCommand(playerDeleteCommand), PlayerCommand {

    override val permissions: List<String> = listOf(
            home_command_delete,
            home_command_delete_name
    )

    override fun fee(): Double = homes.fee.DELETE_NAME

    override fun configs(): List<Boolean> = listOf(onNamedHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        val homeName = args[0]
        PlayerDataManager.removeNamedHome(player, homeName)
        val message = DELETE_NAMED_HOME.replace(HOME_NAME, homeName)
        send(player, message)
    }
}
