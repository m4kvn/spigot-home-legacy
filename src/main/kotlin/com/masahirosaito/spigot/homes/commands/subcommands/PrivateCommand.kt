package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PrivateCommand(override val plugin: Homes) : SubCommand {
    val options = listOf("on", "off")

    override fun name(): String = "private"

    override fun permission(): String = Permission.home_command_private

    override fun description(): String = "Set your home private or public"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home private (on/off)" to "Set your default home private or public",
            "/home private (on/off) <home_name>" to "Set your named home private or public"
    )

    override fun configs(): List<Boolean> = listOf(plugin.configs.onPrivate)

    override fun isInValidArgs(args: List<String>): Boolean {
        return args.isEmpty() || 2 < args.size || !options.contains(args[0])
    }

    override fun execute(player: Player, args: List<String>) {
        when (args.size) {
            1 -> setDefaultHomePrivate(player, args)
            2 -> setNamedHomePrivate(player, args)
        }
    }

    private fun setDefaultHomePrivate(player: Player, args: List<String>) {
        plugin.homeManager.findDefaultHome(player).isPrivate = isPrivate(args)
        send(player, getResultMessage(isPrivate(args)))
    }

    private fun setNamedHomePrivate(player: Player, args: List<String>) {
        checkConfig(plugin.configs.onNamedHome)
        checkPermission(player, Permission.home_command_private_name)
        plugin.homeManager.findNamedHome(player, args[1]).isPrivate = isPrivate(args)
        send(player, getResultMessage(isPrivate(args), args[1]))
    }

    private fun getResultMessage(isPrivate: Boolean, name: String? = null): String = buildString {
        append("Set your ")
        append(if (name == null) "default home " else "home named ${ChatColor.LIGHT_PURPLE}$name ")
        append(if (isPrivate) "${ChatColor.YELLOW}PRIVATE" else "${ChatColor.AQUA}PUBLIC")
        append(ChatColor.RESET)
    }

    private fun isPrivate(args: List<String>) = args[0] == options[0]
}
