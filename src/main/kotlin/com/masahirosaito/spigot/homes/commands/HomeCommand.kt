package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.subcommands.*
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.homedata.HomeData
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class HomeCommand(plugin: Homes) : MainCommand(plugin) {

    override fun name(): String = "home"

    override fun permission(): String = Permission.home_command

    override fun description(): String = "Homes Command"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home" to "Teleport to your default home",
            "/home <home_name>" to "Teleport to your named home",
            "/home -p <player_name>" to "Teleport to player's default home",
            "/home <home_name> -p <player_name>" to "Teleport to player's named home"
    )

    override fun options(): List<String> = listOf("-p")

    override fun isInValidArgs(args: List<String>): Boolean = when (args.size) {
        0, 1 -> args.hasOptions()
        2 -> !options().contains(args[0])
        3 -> !options().contains(args[1])
        else -> true
    }

    override val subCommands = listOf(
            SetCommand(plugin),
            ListCommand(plugin),
            DeleteCommand(plugin),
            HelpCommand(plugin, this),
            PrivateCommand(plugin),
            InviteCommand(plugin)
    )

    override fun execute(player: Player, args: List<String>) {
        when (args.size) {
            0 -> teleportHome(player)
            1 -> teleportHome(player, args.first())
            2 -> teleportPlayerHome(player, args.last())
            3 -> teleportPlayerHome(player, args.getOptionArg(options(0)), args.first())
        }
    }

    private fun getDefaultHome(offlinePlayer: OfflinePlayer): HomeData {
        return plugin.homeManager.findDefaultHome(offlinePlayer)
    }

    private fun getNamedHome(offlinePlayer: OfflinePlayer, homeName: String): HomeData {
        return plugin.homeManager.findNamedHome(offlinePlayer, homeName)
    }

    private fun teleportHome(player: Player, homeName: String? = null) {
        val homeData = if (homeName == null) {
            getDefaultHome(player)
        } else {
            checkConfig(plugin.configs.onNamedHome)
            checkPermission(player, Permission.home_command_name)
            getNamedHome(player, homeName)
        }

        player.teleport(homeData.location())
    }

    private fun teleportPlayerHome(player: Player, playerName: String, homeName: String? = null) {
        checkConfig(plugin.configs.onFriendHome)
        checkPermission(player, Permission.home_command_player)

        val offlinePlayer = findOfflinePlayer(playerName)

        val homeData = if (homeName == null) {
            getDefaultHome(offlinePlayer)
        } else {
            checkConfig(plugin.configs.onNamedHome)
            checkPermission(player, Permission.home_command_name)
            checkPermission(player, Permission.home_command_player_name)
            getNamedHome(offlinePlayer, homeName)
        }.checkPrivate(offlinePlayer, homeName)

        player.teleport(homeData.location())
    }
}
