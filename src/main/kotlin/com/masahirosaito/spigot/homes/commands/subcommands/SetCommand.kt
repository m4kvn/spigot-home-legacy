package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetCommand(override val plugin: Homes) : SubCommand {
    override val name = "set"
    override val permission = Permission.home_command_set
    override var resultMessage = ""
    override val usage = buildString {
        append("${ChatColor.GOLD}Set Command Usage:\n")
        append("${ChatColor.AQUA}/home set${ChatColor.RESET} : Set your location to your default home\n")
        append("${ChatColor.AQUA}/home set <home_name>${ChatColor.RESET} : Set your location to your named home")
    }

    override fun execute(player: Player, args: List<String>) {
        plugin.homedata.playerHomes.put(
                player.uniqueId, (plugin.homedata.playerHomes[player.uniqueId] ?: PlayerHome()).apply {
            LocationData.new(player.location).let {
                resultMessage = buildString {
                    append(ChatColor.AQUA)
                    append("Successfully set as ")
                    append(ChatColor.GOLD)

                    if (args.isEmpty()) {
                        defaultHome = it
                        append("default home")
                    } else {

                        if (!player.hasPermission(Permission.home_command_set_name)) {
                            throw NotHavePermissionException(Permission.home_command_set_name)
                        }

                        namedHomes.put(args[0], it)
                        append("named home <${ChatColor.RESET}${args[0]}${ChatColor.GOLD}>")
                    }

                    append(ChatColor.RESET)
                }
            }
        })
    }
}