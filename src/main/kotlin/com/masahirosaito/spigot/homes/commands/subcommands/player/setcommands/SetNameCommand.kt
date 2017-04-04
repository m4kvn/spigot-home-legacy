package com.masahirosaito.spigot.homes.commands.subcommands.player.setcommands

import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.Permission.home_command_set
import com.masahirosaito.spigot.homes.Permission.home_command_set_name
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.SET_NAMED_HOME
import org.bukkit.entity.Player

class SetNameCommand(setCommand: SetCommand) : SubCommand(setCommand), PlayerCommand {
    override val permissions: List<String> = listOf(home_command_set, home_command_set_name)

    override fun configs(): List<Boolean> = listOf(onNamedHome)

    override fun fee(): Double = homes.fee.SET_NAME

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        PlayerDataManager.setNamedHome(player, player.location, args[0])
        send(player, SET_NAMED_HOME(args[0]))
    }
}
