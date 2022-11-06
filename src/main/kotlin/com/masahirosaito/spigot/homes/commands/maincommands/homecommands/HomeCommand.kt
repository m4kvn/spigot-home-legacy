package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.DelayTeleporter
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.maincommands.MainCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.helpcommands.ConsoleHelpCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.listcommands.ConsoleListCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.reloadcommands.ConsoleReloadCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.deletecommands.PlayerDeleteCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.helpcommands.PlayerHelpCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.invitecommands.PlayerInviteCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.listcommands.PlayerListCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.privatecommands.PlayerPrivateCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.reloadcommands.PlayerReloadCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.setcommands.PlayerSetCommand
import com.masahirosaito.spigot.homes.exceptions.DefaultHomePrivateException
import com.masahirosaito.spigot.homes.exceptions.NamedHomePrivateException
import com.masahirosaito.spigot.homes.strings.commands.HomeCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.HomeCommandStrings.USAGE_HOME
import com.masahirosaito.spigot.homes.strings.commands.HomeCommandStrings.USAGE_HOME_NAME
import com.masahirosaito.spigot.homes.strings.commands.HomeCommandStrings.USAGE_HOME_NAME_PLAYER
import com.masahirosaito.spigot.homes.strings.commands.HomeCommandStrings.USAGE_HOME_PLAYER
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class HomeCommand : MainCommand, PlayerCommand {
    override var payNow: Boolean = false
    override val name: String = "home"
    override val description: String = DESCRIPTION
    override val permissions: List<String> = listOf(
        Permission.home_command
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
        "/home" to USAGE_HOME,
        "/home <home_name>" to USAGE_HOME_NAME,
        "/home -p <player_name>" to USAGE_HOME_PLAYER,
        "/home <home_name> -p <player_name>" to USAGE_HOME_NAME_PLAYER
    ))
    override val playerSubCommands: List<PlayerCommand> = listOf(
            PlayerSetCommand(),
            PlayerDeleteCommand(),
            PlayerPrivateCommand(),
            PlayerInviteCommand(),
            PlayerListCommand(),
            PlayerHelpCommand(),
            PlayerReloadCommand()
    )
    override val consoleSubCommands: List<ConsoleCommand> = listOf(
            ConsoleHelpCommand(),
            ConsoleListCommand(),
            ConsoleReloadCommand()
    )
    override val commands: List<PlayerCommand> = listOf(
            HomeNameCommand(this),
            HomePlayerCommand(this),
            HomeNamePlayerCommand(this)
    )

    init {
        homeCommand = this
    }

    companion object {
        lateinit var homeCommand: HomeCommand
    }

    override fun fee(): Double = homes.fee.HOME

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        DelayTeleporter.run(player, getTeleportLocation(player), this)
    }

    fun getTeleportLocation(player: OfflinePlayer, homeName: String? = null): Location {
        return if (homeName == null) {
            PlayerDataManager.findDefaultHome(player).apply {
                if (isPrivate && !isOwner(player)) throw DefaultHomePrivateException(player)
            }.location
        } else {
            PlayerDataManager.findNamedHome(player, homeName).apply {
                if (isPrivate && !isOwner(player)) throw NamedHomePrivateException(player, homeName)
            }.location
        }
    }
}
