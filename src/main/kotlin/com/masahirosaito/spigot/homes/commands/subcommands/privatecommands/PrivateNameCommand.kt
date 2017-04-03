package com.masahirosaito.spigot.homes.commands.subcommands.privatecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_NAMED_HOME_PRIVATE
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_NAMED_HOME_PUBLIC
import org.bukkit.entity.Player

class PrivateNameCommand(privateCommand: PrivateCommand) : SubCommand(privateCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_private_name
    )

    override fun fee(): Double = plugin.fee.PRIVATE_NAME

    override fun configs(): List<Boolean> = listOf(
            Configs.onPrivate,
            Configs.onNamedHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2 && (args[0] == "on" || args[0] == "off")

    override fun execute(player: Player, args: List<String>) {
        if (args[0] == "on") {
            PlayerDataManager.setNamedHomePrivate(player, args[1], true)
            send(player, SET_NAMED_HOME_PRIVATE(args[1]))
        } else {
            PlayerDataManager.setNamedHomePrivate(player, args[1], false)
            send(player, SET_NAMED_HOME_PUBLIC(args[1]))
        }
    }
}
