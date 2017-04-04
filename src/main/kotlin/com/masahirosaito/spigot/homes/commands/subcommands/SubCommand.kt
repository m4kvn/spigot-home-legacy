package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage

abstract class SubCommand(command: BaseCommand) : BaseCommand {
    override val homes: Homes = command.homes
    override val name: String = command.name
    override val description: String = command.description
    override val commands: List<BaseCommand> = listOf()
    override val usage: CommandUsage = command.usage
}
