package com.masahirosaito.spigot.homes.commands.subcommands.player.listcommands

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_list
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.DESCRIPTION_PLAYER_COMMAND
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.HOME_LIST
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.USAGE_LIST_PLAYER
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.USAGE_PLAYER_COMMAND_LIST
import org.bukkit.entity.Player

class PlayerListCommand : PlayerCommand {
    override val name: String = "list"
    override val description: String = DESCRIPTION_PLAYER_COMMAND()
    override val permissions: List<String> = listOf(home_command_list)
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home list" to USAGE_PLAYER_COMMAND_LIST(),
            "/home list <player_name>" to USAGE_LIST_PLAYER()
    ))
    override val commands: List<BaseCommand> = listOf(
            PlayerListPlayerCommand(this)
    )

    override fun fee(): Double = homes.fee.LIST

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        send(player, HOME_LIST(PlayerDataManager.findPlayerData(player), false))
    }
}
