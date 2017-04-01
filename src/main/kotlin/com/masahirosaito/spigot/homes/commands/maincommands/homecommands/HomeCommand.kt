package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.MainCommand
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.deletecommands.DeleteCommand
import com.masahirosaito.spigot.homes.commands.subcommands.helpcommands.HelpCommand
import com.masahirosaito.spigot.homes.commands.subcommands.invitecommands.InviteCommand
import com.masahirosaito.spigot.homes.commands.subcommands.listcommands.ListCommand
import com.masahirosaito.spigot.homes.commands.subcommands.privatecommands.PrivateCommand
import com.masahirosaito.spigot.homes.commands.subcommands.setcommands.SetCommand
import com.masahirosaito.spigot.homes.exceptions.PrivateHomeException
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class HomeCommand(override val plugin: Homes) : MainCommand, PlayerCommand {
    override val name: String = "home"
    override val description: String = "Homes Command"
    override val permissions: List<String> = listOf(
            Permission.home_command
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home" to "Teleport to your default home",
            "/home <home_name>" to "Teleport to your named home",
            "/home -p <player_name>" to "Teleport to player's default home",
            "/home <home_name> -p <player_name>" to "Teleport to player's named home"
    ))
    override val subCommands: List<BaseCommand> = listOf(
            SetCommand(plugin),
            DeleteCommand(plugin),
            PrivateCommand(plugin),
            InviteCommand(plugin),
            ListCommand(plugin),
            HelpCommand(this)
    )
    override val commands: List<BaseCommand> = listOf(
            HomeNameCommand(this),
            HomePlayerCommand(this),
            HomeNamePlayerCommand(this)
    )

    override fun fee(): Double = plugin.fee.HOME

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        player.teleport(getTeleportLocation(player))
    }

    fun getTeleportLocation(player: OfflinePlayer, homeName: String? = null): Location {
        if (homeName == null) {
            return PlayerDataManager.findDefaultHome(player).apply {
                if (isPrivate && !isOwner(player)) throw PrivateHomeException(player)
            }.location
        } else {
            return PlayerDataManager.findNamedHome(player, homeName).apply {
                if (isPrivate && !isOwner(player)) throw PrivateHomeException(player, homeName)
            }.location
        }
    }
}
