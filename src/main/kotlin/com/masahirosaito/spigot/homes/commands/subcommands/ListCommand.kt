package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.exceptions.CanNotFindOfflinePlayerException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindPlayerHomeException
import com.masahirosaito.spigot.homes.exceptions.NotAllowedByConfigException
import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
import com.masahirosaito.spigot.homes.homedata.LocationData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class ListCommand(override val plugin: Homes) : SubCommand {
    override val name = "list"
    override val permission = Permission.home_command_list
    override var resultMessage = ""
    override val description = "Display the list of your homes"
    override val usage = buildString {
        append("${ChatColor.GOLD}List Command Usage:\n")
        append("${ChatColor.AQUA}/home list${ChatColor.RESET} : Display the list of set homes\n")
        append("${ChatColor.AQUA}/home list <player_name>${ChatColor.RESET} : Display the list of player's set homes")
    }

    override fun execute(player: Player, args: List<String>) {

        resultMessage = when {
            args.isEmpty() -> getHomeList(player, false)
            else -> getPlayerHomeList(player, args)
        }
    }

    private fun getHomeList(player: OfflinePlayer, isPlayerHomeList: Boolean): String {
        val playerHome = plugin.homeManager.playerHomes[player.uniqueId]
                ?: throw CanNotFindPlayerHomeException(player)

        return buildString {
            append("Home List")
            playerHome.defaultHomeData?.let {
                if (!isPlayerHomeList || !it.isPrivate) {
                    append("\n  [${ChatColor.GOLD}Default${ChatColor.RESET}] " +
                            getText(it.locationData, it.isPrivate))
                }
            }

            val namedHomeData = playerHome.namedHomeData
                    .filter { !isPlayerHomeList || !it.value.isPrivate }

            if (namedHomeData.isNotEmpty()) {
                append("\n  [${ChatColor.GOLD}Named Home${ChatColor.RESET}]\n")
                namedHomeData.forEach {
                    append("    ${ChatColor.LIGHT_PURPLE}${it.key}${ChatColor.RESET}")
                    append(" : ${getText(it.value.locationData, it.value.isPrivate)}\n")
                }
            }
        }
    }

    private fun getPlayerHomeList(player: Player, args: List<String>): String {

        if (!plugin.configs.onFriendHome) {
            throw NotAllowedByConfigException()
        }

        if (!player.hasPermission(Permission.home_command_list_player)) {
            throw NotHavePermissionException(Permission.home_command_list_player)
        }

        val playerName = args[0]
        val offlinePlayer = Bukkit.getOfflinePlayers().find { it.name == playerName }
                ?: throw CanNotFindOfflinePlayerException(playerName)

        return getHomeList(offlinePlayer, true)
    }

    private fun getText(ld: LocationData, isPrivate: Boolean): String {
        val r = ChatColor.RESET
        val g = ChatColor.GREEN
        val a = ChatColor.AQUA
        val y = ChatColor.YELLOW
        val b = ChatColor.BLUE

        return buildString {
            append("$g${Bukkit.getWorld(ld.worldUid).name}$r, ")
            append("{$a${ld.x.toInt()}$r, $a${ld.y.toInt()}$r, $a${ld.z.toInt()}$r}, ")
            append(if (isPrivate) "${y}PRIVATE$r" else "${b}PUBLIC$r")
        }
    }
}
