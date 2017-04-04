package com.masahirosaito.spigot.homes.commands.subcommands.player.invitecommands

import com.masahirosaito.spigot.homes.Configs.onFriendHome
import com.masahirosaito.spigot.homes.Configs.onInvite
import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.Permission.home_command_invite
import com.masahirosaito.spigot.homes.Permission.home_command_invite_name
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.RECEIVE_NAMED_HOME_INVITATION_FROM
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.SEND_NAMED_HOME_INVITATION_TO
import org.bukkit.entity.Player

class InvitePlayerNameCommand(val inviteCommand: InviteCommand) : SubCommand(inviteCommand), PlayerCommand {
    override val permissions: List<String> = listOf(home_command_invite, home_command_invite_name)

    override fun fee(): Double = homes.fee.INVITE_PLAYER_NAME

    override fun configs(): List<Boolean> = listOf(onInvite, onNamedHome, onFriendHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2

    override fun execute(player: Player, args: List<String>) {
        inviteNamedHome(player, args[0], args[1])
    }

    private fun inviteNamedHome(player: Player, playerName: String, homeName: String) {
        val entity = PlayerDataManager.findNamedHome(player, homeName)
        inviteCommand.inviteHome(entity, player, playerName,
                RECEIVE_NAMED_HOME_INVITATION_FROM(player.name, homeName))
        send(player, SEND_NAMED_HOME_INVITATION_TO(playerName, homeName))
    }
}
