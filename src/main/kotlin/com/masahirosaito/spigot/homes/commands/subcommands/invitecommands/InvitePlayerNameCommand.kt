package com.masahirosaito.spigot.homes.commands.subcommands.invitecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class InvitePlayerNameCommand(val inviteCommand: InviteCommand) : SubCommand(inviteCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_invite_name
    )

    override fun fee(): Double = plugin.fee.INVITE_PLAYER_NAME

    override fun configs(): List<Boolean> = listOf(
            Configs.onInvite,
            Configs.onNamedHome,
            Configs.onFriendHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2

    override fun execute(player: Player, args: List<String>) {
        inviteNamedHome(player, args[0], args[1])
    }

    private fun inviteNamedHome(player: Player, playerName: String, homeName: String) {
        val entity = PlayerDataManager.findNamedHome(player, homeName)
        inviteCommand.inviteHome(entity, player, playerName, msgReceiveInvitationFrom(player.name, homeName))
        send(player, msgInvite(playerName, homeName))
    }

    private fun msgInvite(playerName: String, homeName: String) = buildString {
        append("${ChatColor.YELLOW}You invited ${ChatColor.RESET}$playerName${ChatColor.YELLOW}")
        append(" to your home named <${ChatColor.RESET}$homeName${ChatColor.YELLOW}>${ChatColor.RESET}")
    }

    private fun msgReceiveInvitationFrom(playerName: String, homeName: String) = buildString {
        append("${ChatColor.YELLOW}You have been invited from")
        append(" ${ChatColor.RESET}$playerName${ChatColor.YELLOW}")
        append(" to home named <${ChatColor.RESET}$homeName${ChatColor.YELLOW}>.\n")
        append("To accept an invitation, please run ${ChatColor.AQUA}/home invite")
        append(" ${ChatColor.YELLOW}within ${ChatColor.LIGHT_PURPLE}30 seconds${ChatColor.RESET}")
    }
}
