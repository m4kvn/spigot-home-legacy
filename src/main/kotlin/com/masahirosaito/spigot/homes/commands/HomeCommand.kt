package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.subcommands.*
import com.masahirosaito.spigot.homes.exceptions.CommandArgumentIncorrectException
import com.masahirosaito.spigot.homes.exceptions.PlayerHomeIsPrivateException
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class HomeCommand(plugin: Homes) : MainCommand(plugin) {

    override fun name(): String = "home"

    override fun permission(): String = Permission.home_command

    override fun description(): String = "Homes Command"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home" to "Teleport to your default home",
            "/home -n <home_name>" to "Teleport to your named home",
            "/home -p <player_name>" to "Teleport to player's default home",
            "/home -p <player_name> -n <home_name>" to "Teleport to player's named home"
    )

    override fun configs(): List<Boolean> = listOf()

    override fun isInValidArgs(args: List<String>): Boolean = false

    override val subCommands = listOf(
            SetCommand(plugin),
            ListCommand(plugin),
            DeleteCommand(plugin),
            HelpCommand(plugin, this),
            PrivateCommand(plugin)
    )

    override fun execute(player: Player, args: List<String>) {
        val isPlayerHome = args.contains(Option.player)
        val isNamedHome = args.contains(Option.name)

        val p = if (isPlayerHome) getPlayer(player, args) else player
        val name = if (isNamedHome) getHomeName(player, p, args) else ""

        val playerHome = plugin.homeManager.findPlayerHome(player)
        val homeData = getHomeData(p, playerHome, name)

        if (isPlayerHome && homeData.isPrivate)
            throw PlayerHomeIsPrivateException(player, name)

        player.teleport(homeData.locationData.toLocation())
    }

    private fun getPlayer(player: Player, args: List<String>): OfflinePlayer {
        checkConfig(plugin.configs.onFriendHome)
        checkPermission(player, Permission.home_command_player)
        return findOfflinePlayer(args.drop(args.indexOf(Option.player) + 1).firstOrNull() ?:
                throw CommandArgumentIncorrectException(this))
    }

    private fun getHomeName(player: Player, offlinePlayer: OfflinePlayer, args: List<String>): String {
        checkConfig(plugin.configs.onNamedHome)
        checkPermission(player, Permission.home_command_name)

        if (offlinePlayer != player) {
            checkPermission(player, Permission.home_command_player_name)
        }

        return args.drop(args.indexOf(Option.name) + 1).firstOrNull() ?:
                throw CommandArgumentIncorrectException(this)
    }

    private fun getHomeData(player: OfflinePlayer, playerHome: PlayerHome, name: String): HomeData {
        return if (name.isNullOrBlank()) {
            playerHome.findDefaultHome(player)
        } else {
            playerHome.findNamedHome(player, name)
        }
    }
}
