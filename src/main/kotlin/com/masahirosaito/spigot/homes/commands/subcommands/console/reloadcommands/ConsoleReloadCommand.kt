package com.masahirosaito.spigot.homes.commands.subcommands.console.reloadcommands

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.strings.commands.ReloadCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.ReloadCommandStrings.USAGE_RELOAD
import org.bukkit.command.ConsoleCommandSender

class ConsoleReloadCommand : ConsoleCommand {
    override val name: String = "reload"
    override val description: String = DESCRIPTION
    override val commands: List<BaseCommand> = listOf()
    override val usage: CommandUsage = CommandUsage(this, listOf(
        "home reload" to USAGE_RELOAD
    ))

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        homes.reload()
    }

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()
}
