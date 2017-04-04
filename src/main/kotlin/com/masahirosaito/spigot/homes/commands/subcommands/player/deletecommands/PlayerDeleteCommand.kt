package com.masahirosaito.spigot.homes.commands.subcommands.player.deletecommands

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_delete
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.USAGE_DELETE
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.USAGE_DELETE_NAME
import org.bukkit.entity.Player

class PlayerDeleteCommand : PlayerCommand {
    override val name: String = "delete"
    override val description: String = DESCRIPTION()
    override val permissions: List<String> = listOf(home_command_delete)
    override val commands: List<BaseCommand> = listOf(
            PlayerDeleteNameCommand(this)
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home delete" to USAGE_DELETE(),
            "/home delete <home_name>" to USAGE_DELETE_NAME()
    ))

    override fun fee(): Double = homes.fee.DELETE

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        PlayerDataManager.removeDefaultHome(player)
        send(player, DELETE_DEFAULT_HOME())
    }
}
