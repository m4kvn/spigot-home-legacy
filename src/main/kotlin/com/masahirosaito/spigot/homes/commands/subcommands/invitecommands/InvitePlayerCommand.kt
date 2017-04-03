package com.masahirosaito.spigot.homes.commands.subcommands.invitecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.RECEIVE_DEFAULT_HOME_INVITATION_FROM
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.SEND_DEFAULT_HOME_INVITATION_TO
import org.bukkit.entity.Player

class InvitePlayerCommand(val inviteCommand: InviteCommand) : SubCommand(inviteCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_invite
    )

    override fun fee(): Double = homes.fee.INVITE_PLAYER

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
        inviteCommand.inviteHome(entity, player, playerName,
                RECEIVE_DEFAULT_HOME_INVITATION_FROM(player.name))
        send(player, SEND_DEFAULT_HOME_INVITATION_TO(playerName))
    }
}
