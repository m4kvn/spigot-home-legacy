package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes

abstract class SubCommand(command: BaseCommand) : BaseCommand {
    override val plugin: Homes = command.plugin
    override val name: String = command.name
    override val description: String = command.description
    override val usage: CommandUsage = command.usage
    override val commands: List<BaseCommand> = listOf()
}
