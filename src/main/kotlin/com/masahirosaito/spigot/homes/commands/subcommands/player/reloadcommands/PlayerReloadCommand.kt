package com.masahirosaito.spigot.homes.commands.subcommands.player.reloadcommands

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_reload
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.ReloadCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.ReloadCommandStrings.USAGE_RELOAD
import org.bukkit.entity.Player

class PlayerReloadCommand : PlayerCommand {
    override var payNow: Boolean = true
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

    override fun execute(player: Player, args: List<String>) {
        homes.reload()
    }
}
