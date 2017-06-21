package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand

abstract class PlayerSubCommand(command: PlayerCommand) : SubCommand(command), PlayerCommand {
    override var payNow: Boolean = command.payNow
}
