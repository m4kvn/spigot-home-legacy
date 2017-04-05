package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command
import com.masahirosaito.spigot.homes.Permission.home_command_player_name
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import org.bukkit.entity.Player

class HomeNamePlayerCommand(val homeCommand: HomeCommand) : SubCommand(homeCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            home_command,
            home_command_player_name
    )

    override fun fee(): Double = homes.fee.HOME_NAME_PLAYER

    override fun configs(): List<Boolean> = listOf(onNamedHome, onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 3 && args[1] == "-p"

    override fun execute(player: Player, args: List<String>) {
        player.teleport(homeCommand.getTeleportLocation(findOfflinePlayer(args[2]), args[0]))
    }
}
