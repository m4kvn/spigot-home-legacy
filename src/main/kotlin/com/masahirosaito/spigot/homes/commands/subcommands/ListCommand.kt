package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.HomesException
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.findPlayerHome
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ListCommand(override val plugin: Homes) : PlayerCommand {
    override val name: String = "list"
    override val description: String = "Display the list of your homes"
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_list
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home list" to "Display the list of homes",
            "/home list <player_name>" to "Display the list of player's homes"
    ))
    override val commands: List<BaseCommand> = listOf(
            ListPlayerCommand(this)
    )

    override fun fee(): Double = plugin.fee.LIST

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        listHome(player)
    }

    class ListPlayerCommand(val listCommand: ListCommand) : SubCommand(listCommand), PlayerCommand {
        override val permissions: List<String> = listOf(
                Permission.home_command,
                Permission.home_command_list_player
        )

        override fun fee(): Double = plugin.fee.LIST_PLAYER

        override fun configs(): List<Boolean> = listOf(
                plugin.configs.onFriendHome
        )

        override fun isValidArgs(args: List<String>): Boolean = args.size == 1

        override fun execute(player: Player, args: List<String>) {
            listCommand.listPlayerHome(player, args)
        }
    }

    private fun listHome(player: Player) {
        send(player, getResultMessage(player.findPlayerHome(plugin), false))
    }

    private fun listPlayerHome(player: Player, args: List<String>) {
        send(player, getResultMessage(findOfflinePlayer(args[0]).findPlayerHome(plugin), true))
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
            throw Exception("No homes")
        }
        if (isPlayerHomeList
                .and(playerHome.defaultHomeData != null && playerHome.defaultHomeData!!.isPrivate)
                .and(playerHome.namedHomeData.all { it.isPrivate })) {
            throw HomesException("No homes")
        }
        append("Home List")
        playerHome.defaultHomeData?.let {
            if (!isPlayerHomeList || !it.isPrivate) {
                append("\n  [${ChatColor.GOLD}Default${ChatColor.RESET}] ${getText(it)}")
            }
        }
        if (plugin.configs.onNamedHome) {
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
}
