package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.exceptions.CommandArgumentIncorrectException
import com.masahirosaito.spigot.homes.exceptions.NotAllowedByConfigException
import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetCommand(override val plugin: Homes) : SubCommand {
    override val name = "set"
    override val permission = Permission.home_command_set
    override var resultMessage = ""
    override val description = "Set your home"
    override val usage = buildString {
        append("${ChatColor.GOLD}Set Command Usage:\n")
        append("${ChatColor.AQUA}/home set${ChatColor.RESET} : Set your location to your default home\n")
        append("${ChatColor.AQUA}/home set <home_name>${ChatColor.RESET} : Set your location to your named home")
    }

    override fun execute(player: Player, args: List<String>) {

        if (args.size > 1) throw CommandArgumentIncorrectException(this)

        when (args.size) {
            0 -> setDefaultHome(player)
            1 -> setNamedHome(player, args[0])
        }
    }

    private fun setDefaultHome(player: Player) {
        val playerHome = playerHome(player)
        val location = LocationData.new(player.location)
        val homeData = playerHome.defaultHomeData

        if (homeData != null) {
            homeData.locationData = location
        } else {
            playerHome.defaultHomeData = HomeData(location)
        }

        resultMessage = "${ChatColor.AQUA}Successfully set as ${ChatColor.GOLD}default home${ChatColor.RESET}"
    }

    private fun setNamedHome(player: Player, name: String) {
        checkNamedHomeConfig()
        checkSetNamePermission(player)

        val playerHome = playerHome(player)
        val location = LocationData.new(player.location)
        val homeData = playerHome.namedHomeData[name]

        if (homeData != null) {
            homeData.locationData = location
        } else {
            checkLimit(playerHome)
            playerHome.namedHomeData.put(name, HomeData(location))
        }

        resultMessage = buildString {
            append("${ChatColor.AQUA}Successfully set as ${ChatColor.GOLD}")
            append("home named <${ChatColor.RESET}$name${ChatColor.GOLD}>")
            append(ChatColor.RESET)
        }
    }

    private fun checkNamedHomeConfig() {
        if (!plugin.configs.onNamedHome) throw NotAllowedByConfigException()
    }

    private fun checkSetNamePermission(player: Player) {
        if (!player.hasPermission(Permission.home_command_set_name))
            throw NotHavePermissionException(Permission.home_command_set_name)
    }

    private fun playerHome(player: Player) = plugin.homeManager.playerHomes[player.uniqueId] ?:
            PlayerHome().apply { plugin.homeManager.playerHomes.put(player.uniqueId, this) }

    private fun checkLimit(playerHome: PlayerHome) {
        if (plugin.configs.homeLimit != -1 && plugin.configs.homeLimit <= playerHome.namedHomeData.size) {
            throw Exception("You can not set more homes " +
                    "(Limit: ${ChatColor.RESET}${plugin.configs.homeLimit}${ChatColor.RED})")
        }
    }
}
