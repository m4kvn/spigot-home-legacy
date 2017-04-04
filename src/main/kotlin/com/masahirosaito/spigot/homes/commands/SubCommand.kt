package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes

abstract class SubCommand(command: BaseCommand) : BaseCommand {
    override val homes: Homes = command.homes
    override val name: String = command.name
    override val description: String = command.description
    override val commands: List<BaseCommand> = listOf()
    override val playerCommandUsage: CommandUsage = command.playerCommandUsage
    override val consoleCommandUsage: CommandUsage = command.consoleCommandUsage
}
