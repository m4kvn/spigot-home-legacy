package com.masahirosaito.spigot.homes.commands.subcommands.player.privatecommands

import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.Configs.onPrivate
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_private
import com.masahirosaito.spigot.homes.Permission.home_command_private_name
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerSubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.createSetNamedHomePrivateMessage
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.createSetNamedHomePublicMessage
import org.bukkit.entity.Player

class PlayerPrivateNameCommand(playerPrivateCommand: PlayerPrivateCommand) :
        PlayerSubCommand(playerPrivateCommand), PlayerCommand {

    override val permissions: List<String> = listOf(home_command_private, home_command_private_name)

    override fun fee(): Double = homes.fee.PRIVATE_NAME

    override fun configs(): List<Boolean> = listOf(onPrivate, onNamedHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2 && (args[0] == "on" || args[0] == "off")

    override fun execute(player: Player, args: List<String>) {
        if (args[0] == "on") {
            PlayerDataManager.setNamedHomePrivate(player, args[1], true)
            send(player, createSetNamedHomePrivateMessage(args[1]))
        } else {
            PlayerDataManager.setNamedHomePrivate(player, args[1], false)
            send(player, createSetNamedHomePublicMessage(args[1]))
        }
    }
}
