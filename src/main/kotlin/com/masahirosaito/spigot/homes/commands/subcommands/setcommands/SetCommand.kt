package com.masahirosaito.spigot.homes.commands.subcommands.setcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.SET_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.USAGE_SET
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.USAGE_SET_NAME
import org.bukkit.entity.Player

class SetCommand(override val homes: Homes) : PlayerCommand {
    override val name: String = "set"
    override val description: String = DESCRIPTION()
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_set
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home set" to USAGE_SET(),
            "/home set <home_name>" to USAGE_SET_NAME()
    ))
    override val commands: List<BaseCommand> = listOf(
            SetNameCommand(this)
    )

    override fun fee(): Double = homes.fee.SET

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        PlayerDataManager.setDefaultHome(player, player.location)
        send(player, SET_DEFAULT_HOME())
    }
}
