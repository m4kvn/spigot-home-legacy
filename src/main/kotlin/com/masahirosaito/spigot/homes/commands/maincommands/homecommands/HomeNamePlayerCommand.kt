package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import org.bukkit.entity.Player

class HomeNamePlayerCommand(val homeCommand: HomeCommand) : SubCommand(homeCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_player_name
    )

    override fun fee(): Double = plugin.fee.HOME_NAME_PLAYER

    override fun configs(): List<Boolean> = listOf(
            Configs.onNamedHome,
            Configs.onFriendHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 3 && args[1] == "-p"

    override fun execute(player: Player, args: List<String>) {
        player.teleport(homeCommand.getTeleportLocation(findOfflinePlayer(args[2]), args[0]))
    }
}
