package com.masahirosaito.spigot.homes.commands.subcommands.player.invitecommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.Configs.onInvite
import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_invite
import com.masahirosaito.spigot.homes.Permission.home_command_invite_name
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.PlayerSubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createReceiveNamedHomeInvitationFrom
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createSendNamedHomeInvitationTo
import org.bukkit.entity.Player

class PlayerInvitePlayerNameCommand(
    private val playerInviteCommand: PlayerInviteCommand,
) : PlayerSubCommand(playerInviteCommand), PlayerCommand {

    override val permissions: List<String> = listOf(home_command_invite, home_command_invite_name)

    override fun fee(): Double = homes.fee.INVITE_PLAYER_NAME

    override fun configs(): List<Boolean> = listOf(onInvite, onNamedHome, onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2

    override fun execute(player: Player, args: List<String>) {
        inviteNamedHome(player, args[0], args[1])
    }

    private fun inviteNamedHome(player: Player, playerName: String, homeName: String) {
        val entity = PlayerDataManager.findNamedHome(player, homeName)
        playerInviteCommand.inviteHome(
            entity, player, playerName,
            createReceiveNamedHomeInvitationFrom(player.name, homeName)
        )
        send(player, createSendNamedHomeInvitationTo(playerName, homeName))
    }
}
