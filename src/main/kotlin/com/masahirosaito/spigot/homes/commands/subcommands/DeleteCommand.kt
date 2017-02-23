package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.exceptions.*
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class DeleteCommand(override val plugin: Homes) : SubCommand {
    override val name: String = "delete"
    override val permission: String = Permission.home_command_delete
    override var resultMessage: String = ""
    override val usage: String = buildString {
        append("${ChatColor.GOLD}Delete Command Usage:\n")
        append("${ChatColor.AQUA}/home delete -y${ChatColor.RESET} : Delete your default home\n")
        append("${ChatColor.AQUA}/home delete -y <home_name>${ChatColor.RESET} : Delete your named home")
    }

    override fun execute(player: Player, args: List<String>) {

        when {
            args.isEmpty() || args[0] != "-y" -> throw CommandArgumentIncorrectException(this)
            args.size == 1 -> deleteHome(player)
            else -> deleteNamedHome(player, args[1])
        }
    }

    private fun deleteHome(player: OfflinePlayer) {
        val playerHome = plugin.homedata.playerHomes[player.uniqueId]
                ?: throw CanNotFindPlayerHomeException(player)

        if (playerHome.defaultHome == null)
            throw CanNotFindDefaultHomeException(player)

        playerHome.defaultHome = null
        resultMessage = "${ChatColor.AQUA}Successfully delete your default home${ChatColor.RESET}"
    }

    private fun deleteNamedHome(player: Player, homeName: String) {

        if (!plugin.configs.onNamedHome)
            throw CanNotUseNamedHomeException()

        if (!player.hasPermission(Permission.home_command_delete_name))
            throw NotHavePermissionException(Permission.home_command_delete_name)

        val playerHome = plugin.homedata.playerHomes[player.uniqueId]
                ?: throw CanNotFindPlayerHomeException(player)

        if (playerHome.namedHomes[homeName] == null)
            throw CanNotFindNamedHomeException(player, homeName)

        playerHome.namedHomes.remove(homeName)
        resultMessage = "${ChatColor.AQUA}Successfully delete your named home <" +
                "${ChatColor.RESET}$homeName${ChatColor.AQUA}" +
                ">${ChatColor.RESET}"
    }
}