package com.masahirosaito.spigot.homes.commands.subcommands.listcommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.datas.PlayerData
import com.masahirosaito.spigot.homes.exceptions.HomesException
import com.masahirosaito.spigot.homes.nms.HomesEntity
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

    private fun listHome(player: Player) {
        send(player, getResultMessage(PlayerDataManager.findPlayerData(player), false))
    }

    private fun getText(homesEntity: HomesEntity): String {
        val r = ChatColor.RESET
        val g = ChatColor.GREEN
        val a = ChatColor.AQUA
        val y = ChatColor.YELLOW
        val b = ChatColor.BLUE

        val loc = homesEntity.location

        return buildString {
            append("$g${loc.world.name}$r, ")
            append("{$a${loc.x.toInt()}$r, $a${loc.y.toInt()}$r, $a${loc.z.toInt()}$r}, ")
            append(if (homesEntity.isPrivate) "${y}PRIVATE$r" else "${b}PUBLIC$r")
        }
    }

    fun getResultMessage(playerData: PlayerData, isPlayerHomeList: Boolean) = buildString {
        val defaultHome = playerData.defaultHome
        val namedHomes = playerData.namedHomes

        if (defaultHome == null && namedHomes.isEmpty()) {
            throw HomesException("No homes")
        }

        if (isPlayerHomeList
                .and(defaultHome != null && defaultHome.isPrivate)
                .and(namedHomes.all { it.isPrivate })) {
            throw HomesException("No homes")
        }

        append("Home List")

        defaultHome?.let {
            if (!isPlayerHomeList || !it.isPrivate) {
                append("\n  [${ChatColor.GOLD}Default${ChatColor.RESET}] ${getText(it)}")
            }
        }

        if (Configs.onNamedHome) {
            namedHomes.filter { !isPlayerHomeList || !it.isPrivate }.apply {
                if (isNotEmpty()) {
                    append("\n  [${ChatColor.GOLD}Named Home${ChatColor.RESET}]\n")
                    this.forEach {
                        append("    ${org.bukkit.ChatColor.LIGHT_PURPLE}${it.homeName}${org.bukkit.ChatColor.RESET}")
                        append(" : ${getText(it)}\n")
                    }
                }
            }
        }
    }
}
