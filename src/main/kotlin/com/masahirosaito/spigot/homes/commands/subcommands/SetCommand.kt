package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetCommand(override val plugin: Homes) : SubCommand {

    override fun name(): String = "set"

    override fun permission(): String = Permission.home_command_set

    override fun description(): String = "Set your home or named home"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home set" to "Set your location to your default home",
            "/home set <home_name>" to "Set your location to your named home"
    )

    override fun isInValidArgs(args: List<String>): Boolean = args.size > 1

    override fun execute(player: Player, args: List<String>) {
        when (args.size) {
            0 -> setDefaultHome(player)
            1 -> setNamedHome(player, args[0])
        }
    }

    private fun setDefaultHome(player: Player) {
        plugin.homeManager.findPlayerHome(player).setDefaultHome(player)
        send(player, getResultMessage())
    }

    private fun setNamedHome(player: Player, name: String) {
        checkConfig(plugin.configs.onNamedHome)
        checkPermission(player, Permission.home_command_set_name)
        plugin.homeManager.findPlayerHome(player).setNamedHome(player, name, plugin.configs.homeLimit)
        send(player, getResultMessage(name))
    }

    private fun getResultMessage(name: String? = null) = buildString {
        append("${ChatColor.AQUA}Successfully set as ${ChatColor.GOLD}")
        append(if (name.isNullOrBlank()) "default home" else "home named <${ChatColor.RESET}$name${ChatColor.GOLD}>")
        append(ChatColor.RESET)
    }
}
