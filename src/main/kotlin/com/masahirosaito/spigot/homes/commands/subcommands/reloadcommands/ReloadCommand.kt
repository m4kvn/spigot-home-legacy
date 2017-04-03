package com.masahirosaito.spigot.homes.commands.subcommands.reloadcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission.home_command_reload
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.ReloadCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.ReloadCommandStrings.USAGE_RELOAD
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class ReloadCommand(override val homes: Homes) : PlayerCommand, ConsoleCommand {
    override val name: String = "reload"
    override val description: String = DESCRIPTION()
    override val commands: List<BaseCommand> = listOf()
    override val permissions: List<String> = listOf(home_command_reload)
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home reload" to USAGE_RELOAD()
    ))

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun fee(): Double = homes.fee.RELOAD

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        homes.reload()
    }

    override fun execute(player: Player, args: List<String>) {
        homes.reload()
    }
}
