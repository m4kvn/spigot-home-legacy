package com.masahirosaito.spigot.homes.commands.subcommands.player.invitecommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.Configs.onInvite
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_invite
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerSubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createReceiveDefaultHomeInvitationFrom
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createSendDefaultHomeInvitationTo
import org.bukkit.entity.Player

class PlayerInvitePlayerCommand(
    private val playerInviteCommand: PlayerInviteCommand,
) : PlayerSubCommand(playerInviteCommand), PlayerCommand {

    override val permissions: List<String> = listOf(home_command_invite)

    override fun fee(): Double = homes.fee.INVITE_PLAYER

    override fun configs(): List<Boolean> = listOf(onInvite, onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        inviteDefaultHome(player, args[0])
    }

    private fun inviteDefaultHome(player: Player, playerName: String) {
        val entity = PlayerDataManager.findDefaultHome(player)
        playerInviteCommand.inviteHome(
            entity, player, playerName,
            createReceiveDefaultHomeInvitationFrom(player.name)
        )
        send(player, createSendDefaultHomeInvitationTo(playerName))
    }
}
