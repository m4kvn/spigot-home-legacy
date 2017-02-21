package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetCommand(override val plugin: Homes) : SubCommand {
    override val name = "set"
    override val permission = Permission.home_command_set
    override val usage = buildString {
        append("${ChatColor.GOLD}Set Command Usage:\n")
        append("${ChatColor.BLUE}/home set${ChatColor.RESET} : Set your home\n")
        append("${ChatColor.BLUE}/home set <home_name>${ChatColor.RESET} : Set your named home")
    }

    val messenger = plugin.messenger

    override fun execute(player: Player, args: List<String>) {
        val playerHome = plugin.homedata.playerHomes[player.uniqueId] ?: PlayerHome()

        if (args.isEmpty()) {
            playerHome.defaultHome = LocationData.new(player.location)
            messenger.send(player, buildString {
                append(ChatColor.AQUA)
                append("Set default home")
                append(ChatColor.RESET)
            })
        } else {
            if (plugin.configs.onNamedHome) {
                playerHome.namedHomes.put(args[0], LocationData.new(player.location))
                messenger.send(player, buildString {
                    append(ChatColor.AQUA)
                    append("Set home <${ChatColor.RESET}${args[0]}${ChatColor.AQUA}>")
                    append(ChatColor.RESET)
                })
            } else {
                messenger.send(player, buildString {
                    append(ChatColor.RED)
                    append("Currently, the named home can not be set")
                    append(ChatColor.RESET)
                })
                return
            }
        }

        plugin.homedata.playerHomes.put(player.uniqueId, playerHome)

        return
    }
}