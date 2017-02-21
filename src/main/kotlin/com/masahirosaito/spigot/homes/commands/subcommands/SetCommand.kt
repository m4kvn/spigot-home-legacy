package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.CommandResult
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetCommand(override val plugin: Homes) : SubCommand {
    override val name = "set"
    override val permission = Permission.home_command_set
    override val result = CommandResult()
    override val usage = buildString {
        append("${ChatColor.GOLD}Set Command Usage:\n")
        append("${ChatColor.BLUE}/home set${ChatColor.RESET} : Set your home\n")
        append("${ChatColor.BLUE}/home set <home_name>${ChatColor.RESET} : Set your named home")
    }

    override fun execute(player: Player, args: List<String>) {
        plugin.homedata.playerHomes.put(
                player.uniqueId, (plugin.homedata.playerHomes[player.uniqueId] ?: PlayerHome()).apply {
            LocationData.new(player.location).let {
                result.message = buildString {
                    append(ChatColor.AQUA)
                    append("Successfully set as ")
                    append(ChatColor.GOLD)

                    if (args.isEmpty()) {
                        defaultHome = it
                        append("default home")
                    } else {
                        namedHomes.put(args[0], it)
                        append("named home <${ChatColor.RESET}${args[0]}${ChatColor.GOLD}>")
                    }

                    append(ChatColor.RESET)
                }
            }
        })
    }
}