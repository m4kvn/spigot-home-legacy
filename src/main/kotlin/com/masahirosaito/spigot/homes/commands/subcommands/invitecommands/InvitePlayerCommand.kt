package com.masahirosaito.spigot.homes.commands.subcommands.invitecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class InvitePlayerCommand(val inviteCommand: InviteCommand) : SubCommand(inviteCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_invite
    )

    override fun fee(): Double = plugin.fee.INVITE_PLAYER

    override fun configs(): List<Boolean> = listOf(
            Configs.onInvite,
            Configs.onFriendHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        inviteDefaultHome(player, args[0])
    }

    private fun inviteDefaultHome(player: Player, playerName: String) {
        val entity = PlayerDataManager.findDefaultHome(player)
        inviteCommand.inviteHome(entity, player, playerName, msgReceiveInvitationFrom(player.name))
        send(player, msgInvite(playerName))
    }

    private fun msgReceiveInvitationFrom(playerName: String) = buildString {
        append("${ChatColor.YELLOW}You have been invited from")
        append(" ${ChatColor.RESET}$playerName${ChatColor.YELLOW} to default home.\n")
        append("To accept an invitation, please run ${ChatColor.AQUA}/home invite")
        append(" ${ChatColor.YELLOW}within ${ChatColor.LIGHT_PURPLE}30 seconds${ChatColor.RESET}")
    }

    private fun msgInvite(playerName: String) = buildString {
        append("${ChatColor.YELLOW}You invited")
        append(" ${ChatColor.RESET}$playerName${ChatColor.YELLOW}")
        append(" to your default home")
    }
}
