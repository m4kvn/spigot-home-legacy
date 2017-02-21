package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.subcommands.SetCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.*
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.ChatColor.*
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand(override val plugin: Homes) : CommandExecutor, SubCommand {
    override val name = "home"
    override val permission = Permission.home_command
    override val usage = buildString {
        append("${GOLD}Home Command Usage:$RESET\n")
        append("$BLUE/home$RESET : Teleport to your set home\n")
        append("$BLUE/home -n <home_name>$RESET : Teleport to your set named home\n")
        append("$BLUE/home -p <player_name>$RESET : Teleport to player's set home\n")
        append("$BLUE/home -p <player_name> -n <home_name>$RESET : Teleport to player's set named home")
    }

    object Args {
        val name = "-n"
        val player = "-p"
    }

    val messenger = plugin.messenger
    val subCommands = listOf<SubCommand>(SetCommand(plugin))

    override fun onCommand(sender: CommandSender?, command: Command?,
                           label: String?, args: Array<out String>?): Boolean {

        if (sender !is Player) return true

        if (!hasPermission(sender)) return sendPermissionMsg(sender)

        if (args == null || args.isEmpty()) return execute(sender, emptyList())

        subCommands.find { it.name == args[0] }?.let {
            return if (it.hasPermission(sender)) {
                it.execute(sender, args.drop(1))
            } else {
                it.sendPermissionMsg(sender)
            }
        }

        return execute(sender, args.toList())
    }

    override fun execute(player: Player, args: List<String>): Boolean {

        try {
            val p = if (args.contains(Args.player)) {
                getPlayer(player, args)
            } else {
                player
            }

            val name = if (args.contains(Args.name)) {
                getHomeName(player, p, args)
            } else {
                ""
            }

            val playerHome = getPlayerHome(p)

            val location = getLocation(p, playerHome, name)

            player.teleport(location)

        } catch (e: Exception) {
            messenger.send(player, buildString {
                append(ChatColor.RED)
                append(when (e) {
                    is CanNotUsePlayerHomeException -> "You can not teleport to Player Home"
                    is NotHavePermissionException -> "You don't have permission <${e.permission}>"
                    is ArrayIndexOutOfBoundsException -> "The argument is incorrect\n$usage"
                    is CanNotFindOfflinePlayerException -> "Player <${e.playerName}> does not exist"
                    is CanNotFindPlayerHomeException -> "${e.player.name}'s home does not exist"
                    is CanNotFindDefaultHomeException -> "${e.player.name}'s default home does not exist"
                    is CanNotFindNamedHomeException -> "${e.player.name}'s named home <${e.name}> does not exist"
                    else -> return true
                })
                append(ChatColor.RESET)
            })
        }

        return true
    }

    private fun getPlayer(player: Player, args: List<String>): OfflinePlayer {

        if (!plugin.configs.onFriendHome) {
            throw CanNotUsePlayerHomeException()
        }

        if (!player.hasPermission(Permission.home_command_player)) {
            throw NotHavePermissionException(Permission.home_command_player)
        }

        val playerName = args.drop(args.indexOf(Args.player) + 1).firstOrNull()
                ?: throw ArrayIndexOutOfBoundsException()

        return Bukkit.getOfflinePlayers()
                .find { it.name == playerName } ?: throw CanNotFindOfflinePlayerException(playerName)

    }

    private fun getHomeName(player: Player, offlinePlayer: OfflinePlayer, args: List<String>): String {

        if (!plugin.configs.onNamedHome) {
            throw CanNotUserNamedHomeException()
        }

        if (!player.hasPermission(Permission.home_command_name)) {
            throw NotHavePermissionException(Permission.home_command_name)
        }

        if (offlinePlayer != player) {
            if (!player.hasPermission(Permission.home_command_player_name)) {
                throw NotHavePermissionException(Permission.home_command_player_name)
            }
        }

        return args.drop(args.indexOf(Args.name) + 1).firstOrNull() ?: throw ArrayIndexOutOfBoundsException()
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