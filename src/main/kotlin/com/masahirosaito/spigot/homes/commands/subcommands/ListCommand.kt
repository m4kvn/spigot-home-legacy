package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.exceptions.CanNotFindPlayerHomeException
import com.masahirosaito.spigot.homes.homedata.LocationData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ListCommand(override val plugin: Homes) : SubCommand {
    override val name = "list"
    override val permission = Permission.home_command_list
    override var resultMessage = ""
    override val usage = buildString {
        append("${ChatColor.GOLD}List Command Usage:\n")
        append("${ChatColor.BLUE}/home list${ChatColor.RESET} : Display the list of set homes\n")
        append("${ChatColor.BLUE}/home list <player_name>${ChatColor.RESET} : Display the list of player's set homes")
    }

    override fun execute(player: Player, args: List<String>) {

        resultMessage = when {
            args.isEmpty() -> getHomeList(player)
            else -> getPlayerHomeList(player, args.drop(1))
        }
    }

    private fun getHomeList(player: Player): String {
        val playerHome = plugin.homedata.playerHomes[player.uniqueId] ?: throw CanNotFindPlayerHomeException(player)

        return buildString {
            append("Home List")
            playerHome.defaultHome?.let { append("\n [${ChatColor.GOLD}Default${ChatColor.RESET}] ${getText(it)}") }

            if (playerHome.namedHomes.isNotEmpty()) {
                append("\n [${ChatColor.GOLD}Named Home${ChatColor.RESET}]\n")
                playerHome.namedHomes.forEach {
                    append(" - ${ChatColor.LIGHT_PURPLE}${it.key}${ChatColor.RESET}: ${getText(it.value)}\n")
                }
            }
        }
    }

    private fun getPlayerHomeList(player: Player, args: List<String>): String {
        return ""
    }

    private fun getText(it: LocationData): String {
        val r = ChatColor.RESET
        val g = ChatColor.GREEN
        val a = ChatColor.AQUA
        val y = ChatColor.YELLOW

        return buildString {
            append("$g${Bukkit.getWorld(it.worldUid).name}$r, ")
            append("x=$a${it.x.toInt()}$r, y=$a${it.y.toInt()}$r, z=$a${it.z.toInt()}$r")
        }
    }
}