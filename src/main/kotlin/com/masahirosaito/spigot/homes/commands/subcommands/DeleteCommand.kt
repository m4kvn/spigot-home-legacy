package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.findPlayerHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class DeleteCommand(override val plugin: Homes) : PlayerCommand {
    override val name: String = "delete"
    override val fee: Double = plugin.fee.DELETE
    override val description: String = "Delete your homes"
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_delete
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home delete" to "Delete your default home",
            "/home delete <home_name>" to "Delete your named home"
    ))
    override val commands: List<BaseCommand> = listOf(
            DeleteNameCommand(this)
    )
    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        player.findPlayerHome(plugin).removeDefaultHome(player)
        send(player, "${ChatColor.AQUA}Successfully delete your default home${ChatColor.RESET}")
    }

    class DeleteNameCommand(deleteCommand: DeleteCommand) : SubCommand(deleteCommand), PlayerCommand {
        override val fee: Double = plugin.fee.DELETE_NAME
        override val permissions: List<String> = listOf(
                Permission.home_command,
                Permission.home_command_delete_name
        )
        override fun configs(): List<Boolean> = listOf(
                plugin.configs.onNamedHome
        )

        override fun isValidArgs(args: List<String>): Boolean = args.size == 1

        override fun execute(player: Player, args: List<String>) {
            player.findPlayerHome(plugin).removeNamedHome(player, args[0])
            send(player, "${ChatColor.AQUA}Successfully delete your named home" +
                    " <${ChatColor.RESET}${args[0]}${ChatColor.AQUA}>${ChatColor.RESET}")
        }
    }
}
