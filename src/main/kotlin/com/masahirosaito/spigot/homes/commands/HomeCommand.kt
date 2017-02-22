package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.subcommands.SetCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.*
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand(override val plugin: Homes) : CommandExecutor, SubCommand {
    override val name = "home"
    override val permission = Permission.home_command
    override var resultMessage = ""
    override val usage = buildString {
        append("${ChatColor.GOLD}Home Command Usage:\n")
        append("${ChatColor.BLUE}/home${ChatColor.RESET} : Teleport to your set default home\n")
        append("${ChatColor.BLUE}/home -n <home_name>${ChatColor.RESET} : Teleport to your set named home\n")
        append("${ChatColor.BLUE}/home -p <player_name>${ChatColor.RESET} : Teleport to player's set default home\n")
        append("${ChatColor.BLUE}/home -p <player_name> -n <home_name>${ChatColor.RESET} : Teleport to player's set named home")
    }

    val messenger = plugin.messenger
    val subCommands = listOf<SubCommand>(SetCommand(plugin))

    override fun onCommand(sender: CommandSender?, command: Command?,
                           label: String?, args: Array<out String>?): Boolean {

        try {
            when {
                sender !is Player -> throw InValidCommandSenderException()
                !hasPermission(sender) -> throw NotHavePermissionException(permission)
                args == null || args.isEmpty() -> execute(sender, emptyList())
                subCommands.any { it.name == args[0] } -> executeSubCommand(args, sender)
                else -> execute(sender, args.toList())
            }

        } catch (e: Exception) {
            messenger.send(sender!!, buildString {
                append(ChatColor.RED)
                append(e.message)
                append(ChatColor.RESET)
            })
        }

        return true
    }

    private fun executeSubCommand(args: Array<out String>, player: Player) {
        val subCommand = subCommands.find { it.name == args[0] }!!

        if (!subCommand.hasPermission(player)) {
            throw NotHavePermissionException(subCommand.permission)
        }

        subCommand.execute(player, args.drop(1))

        if (subCommand.resultMessage.isNotBlank()) {
            messenger.send(player, subCommand.resultMessage)
        }
    }

    override fun execute(player: Player, args: List<String>) {
        val p = if (args.contains(CommandArg.player)) getPlayer(player, args) else player
        val name = if (args.contains(CommandArg.name)) getHomeName(player, p, args) else ""
        val playerHome = getPlayerHome(p)
        val location = getLocation(p, playerHome, name)

        player.teleport(location)
    }

    private fun getPlayer(player: Player, args: List<String>): OfflinePlayer {

        if (!plugin.configs.onFriendHome) {
            throw CanNotUsePlayerHomeException()
        }

        if (!player.hasPermission(Permission.home_command_player)) {
            throw NotHavePermissionException(Permission.home_command_player)
        }

        val playerName = args.drop(args.indexOf(CommandArg.player) + 1).firstOrNull()
                ?: throw CommandArgumentIncorrectException(this)

        return Bukkit.getOfflinePlayers()
                .find { it.name == playerName } ?: throw CanNotFindOfflinePlayerException(playerName)

    }

    private fun getHomeName(player: Player, offlinePlayer: OfflinePlayer, args: List<String>): String {

        if (!plugin.configs.onNamedHome) {
            throw CanNotUseNamedHomeException()
        }

        if (!player.hasPermission(Permission.home_command_name)) {
            throw NotHavePermissionException(Permission.home_command_name)
        }

        if (offlinePlayer != player) {
            if (!player.hasPermission(Permission.home_command_player_name)) {
                throw NotHavePermissionException(Permission.home_command_player_name)
            }
        }

        return args.drop(args.indexOf(CommandArg.name) + 1).firstOrNull() ?: throw CommandArgumentIncorrectException(this)
    }

    private fun getPlayerHome(player: OfflinePlayer): PlayerHome {
        return plugin.homedata.playerHomes[player.uniqueId] ?: throw CanNotFindPlayerHomeException(player)
    }

    private fun getLocation(player: OfflinePlayer, playerHome: PlayerHome, name: String): Location {

        return if (name.isNullOrBlank()) {
            playerHome.defaultHome ?: throw CanNotFindDefaultHomeException(player)
        } else {
            playerHome.namedHomes[name] ?: throw CanNotFindNamedHomeException(player, name)
        }.toLocation()
    }
}