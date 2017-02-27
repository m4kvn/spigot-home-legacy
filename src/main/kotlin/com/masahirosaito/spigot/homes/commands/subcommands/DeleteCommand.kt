package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class DeleteCommand(override val plugin: Homes) : SubCommand {

    override fun name(): String = "delete"

    override fun permission(): String = Permission.home_command_delete

    override fun description(): String = "Delete your home"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home delete" to "Delete your default home",
            "/home delete <home_name>" to "Delete your named home"
    )

    override fun isInValidArgs(args: List<String>): Boolean = args.size > 1

    override fun execute(player: Player, args: List<String>) {
        when (args.size) {
            0 -> deleteHome(player)
            1 -> deleteNamedHome(player, args[0])
        }
    }

    private fun deleteHome(player: Player) {
        plugin.homeManager.findPlayerHome(player).removeDefaultHome(player)
        send(player, getResultMessage())
    }

    private fun deleteNamedHome(player: Player, name: String) {
        checkPermission(player, Permission.home_command_delete_name)
        plugin.homeManager.findPlayerHome(player).removeNamedHome(player, name)
        send(player, getResultMessage(name))
    }

    private fun getResultMessage(name: String? = null): String = buildString {
        append("${ChatColor.AQUA}Successfully delete your ")
        append(if (name == null) "default home" else "named home <${ChatColor.RESET}$name${ChatColor.AQUA}>")
        append(ChatColor.RESET)
    }
}
