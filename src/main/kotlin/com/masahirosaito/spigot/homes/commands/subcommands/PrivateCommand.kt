package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.exceptions.*
import com.masahirosaito.spigot.homes.homedata.HomeData
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PrivateCommand(override val plugin: Homes) : SubCommand {
    override val name: String = "private"
    override val permission: String = Permission.home_command_private
    override var resultMessage: String = ""
    override val description: String = "Set your home private or public"
    override val usage: String = buildString {
        append("${ChatColor.GOLD}Private Command Usage:\n")
        append("${ChatColor.AQUA}/home private (on/off)${ChatColor.RESET}" +
                " : Set your default home private or public\n")
        append("${ChatColor.AQUA}/home private (on/off) <home_name>${ChatColor.RESET}" +
                " : Set your named home private or public")
    }

    private val options = listOf("on", "off")

    override fun execute(player: Player, args: List<String>) {

        if (!plugin.configs.onPrivate)
            throw CanNotUsePrivateHomeException()

        if (args.isEmpty() || 2 < args.size || !options.contains(args[0])) {
            throw CommandArgumentIncorrectException(this)
        }

        when (args.size) {
            1 -> setDefaultHomePrivate(player, args)
            2 -> setNamedHomePrivate(player, args)
        }
    }

    private fun setPrivate(homeData: HomeData, isPrivate: Boolean) {
        homeData.isPrivate = isPrivate
    }

    private fun setDefaultHomePrivate(player: Player, args: List<String>) {
        val playerHome = plugin.homeManager.playerHomes[player.uniqueId] ?:
                throw CanNotFindPlayerHomeException(player)

        val defaultHomeData = playerHome.defaultHomeData ?:
                throw CanNotFindDefaultHomeException(player)

        setPrivate(defaultHomeData, args[0] == options[0])
    }

    private fun setNamedHomePrivate(player: Player, args: List<String>) {

        if (!plugin.configs.onNamedHome)
            throw CanNotUseNamedHomeException()

        val playerHome = plugin.homeManager.playerHomes[player.uniqueId] ?:
                throw CanNotFindPlayerHomeException(player)

        val name = args[1]

        val namedHomeData = playerHome.namedHomeData[name] ?:
                throw CanNotFindNamedHomeException(player, name)

        setPrivate(namedHomeData, args[0] == options[0])
    }
}
