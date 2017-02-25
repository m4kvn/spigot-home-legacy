package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.exceptions.*
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class DeleteCommand(override val plugin: Homes) : SubCommand {
    override val name: String = "delete"
    override val permission: String = Permission.home_command_delete
    override var resultMessage: String = ""
    override val description: String = "Delete your home"
    override val usage: String = buildString {
        append("${ChatColor.GOLD}Delete Command Usage:\n")
        append("${ChatColor.AQUA}/home delete ${ChatColor.RESET} : Delete your default home\n")
        append("${ChatColor.AQUA}/home delete <home_name>${ChatColor.RESET} : Delete your named home")
    }

    override fun execute(player: Player, args: List<String>) {

        when (args.size) {
            0 -> deleteHome(player)
            1 -> deleteNamedHome(player, args[0])
            else -> throw CommandArgumentIncorrectException(this)
        }
    }

    private fun deleteHome(player: Player) {
        val playerHome = plugin.homeManager.playerHomes[player.uniqueId]
                ?: throw CanNotFindPlayerHomeException(player)

        if (playerHome.defaultHomeData == null)
            throw CanNotFindDefaultHomeException(player)

        playerHome.defaultHomeData = null
        resultMessage = buildString {
            append(ChatColor.AQUA)
            append("Successfully delete your default home")
            append(ChatColor.RESET)
        }
    }

    private fun deleteNamedHome(player: Player, name: String) {

        if (!plugin.configs.onNamedHome)
            throw NotAllowedByConfigException()

        if (!player.hasPermission(Permission.home_command_delete_name))
            throw NotHavePermissionException(Permission.home_command_delete_name)

        val playerHome = plugin.homeManager.playerHomes[player.uniqueId]
                ?: throw CanNotFindPlayerHomeException(player)

        if (playerHome.namedHomeData[name] == null)
            throw CanNotFindNamedHomeException(player, name)

        playerHome.namedHomeData.remove(name)
        resultMessage = buildString {
            append(ChatColor.AQUA)
            append("Successfully delete your named home <")
            append(ChatColor.RESET)
            append(name)
            append(ChatColor.AQUA)
            append(">")
            append(ChatColor.RESET)
        }
    }
}
