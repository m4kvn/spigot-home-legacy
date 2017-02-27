package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ListCommand(override val plugin: Homes) : SubCommand {

    override fun name(): String = "list"

    override fun permission(): String = Permission.home_command_list

    override fun description(): String = "Display the list of your homes"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home list" to "Display the list of homes",
            "/home list <player_name>" to "Display the list of player's homes"
    )

    override fun configs(): List<Boolean> = listOf()

    override fun isInValidArgs(args: List<String>): Boolean = args.size > 1

    override fun execute(player: Player, args: List<String>) {

        when (args.size) {
            0 -> listHome(player)
            1 -> listPlayerHome(player, args)
        }
    }

    private fun listHome(player: Player) {
        send(player, getResultMessage(plugin.homeManager.findPlayerHome(player), false))
    }

    private fun listPlayerHome(player: Player, args: List<String>) {
        checkConfig(plugin.configs.onFriendHome)
        checkPermission(player, Permission.home_command_list_player)
        send(player, getResultMessage(plugin.homeManager.findPlayerHome(findOfflinePlayer(args[0])), true))
    }

    private fun getText(homeData: HomeData): String {
        val r = ChatColor.RESET
        val g = ChatColor.GREEN
        val a = ChatColor.AQUA
        val y = ChatColor.YELLOW
        val b = ChatColor.BLUE

        val ld = homeData.locationData

        return buildString {
            append("$g${Bukkit.getWorld(ld.worldUid).name}$r, ")
            append("{$a${ld.x.toInt()}$r, $a${ld.y.toInt()}$r, $a${ld.z.toInt()}$r}, ")
            append(if (homeData.isPrivate) "${y}PRIVATE$r" else "${b}PUBLIC$r")
        }
    }

    private fun getResultMessage(playerHome: PlayerHome, isPlayerHomeList: Boolean) = buildString {
        if (playerHome.defaultHomeData == null && playerHome.namedHomeData.isEmpty()) {
            throw Exception("You have not set any homes")
        }
        append("Home List")
        playerHome.defaultHomeData?.let {
            if (!isPlayerHomeList || !it.isPrivate) {
                append("\n  [${ChatColor.GOLD}Default${ChatColor.RESET}] ${getText(it)}")
            }
        }
        playerHome.namedHomeData.filter { !isPlayerHomeList || !it.isPrivate }.apply {
            if (isNotEmpty()) {
                append("\n  [${ChatColor.GOLD}Named Home${ChatColor.RESET}]\n")
                this.forEach {
                    append("    ${ChatColor.LIGHT_PURPLE}${it.name}${ChatColor.RESET}")
                    append(" : ${getText(it)}\n")
                }
            }
        }
    }
}
