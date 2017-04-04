package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.*
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.maincommands.MainCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.deletecommands.DeleteCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.helpcommands.HelpCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.invitecommands.InviteCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.listcommands.ListCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.privatecommands.PrivateCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.reloadcommands.ReloadCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.setcommands.SetCommand
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

class HomeCommand(override val homes: Homes) : MainCommand, PlayerCommand {
    override val name: String = "home"
    override val description: String = DESCRIPTION()
    override val permissions: List<String> = listOf(
            Permission.home_command
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home" to USAGE_HOME(),
            "/home <home_name>" to USAGE_HOME_NAME(),
            "/home -p <player_name>" to USAGE_HOME_PLAYER(),
            "/home <home_name> -p <player_name>" to USAGE_HOME_NAME_PLAYER()
    ))
    override val playerSubCommands: List<PlayerCommand> = listOf(
            SetCommand(homes),
            DeleteCommand(homes),
            PrivateCommand(homes),
            InviteCommand(homes),
            ListCommand(homes),
            HelpCommand(this),
            ReloadCommand(homes)
    )
    override val consoleSubCommands: List<ConsoleCommand> = listOf(

    )
    override val commands: List<PlayerCommand> = listOf(
            HomeNameCommand(this),
            HomePlayerCommand(this),
            HomeNamePlayerCommand(this)
    )

    override fun fee(): Double = homes.fee.HOME

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        player.teleport(getTeleportLocation(player))
    }

    fun getTeleportLocation(player: OfflinePlayer, homeName: String? = null): Location {
        if (homeName == null) {
            return PlayerDataManager.findDefaultHome(player).apply {
                if (isPrivate && !isOwner(player)) throw DefaultHomePrivateException(player)
            }.location
        } else {
            return PlayerDataManager.findNamedHome(player, homeName).apply {
                if (isPrivate && !isOwner(player)) throw NamedHomePrivateException(player, homeName)
            }.location
        }
    }
}
