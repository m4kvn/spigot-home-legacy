package com.masahirosaito.spigot.homes.commands.subcommands.invitecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.exceptions.AlreadyHasInvitationException
import com.masahirosaito.spigot.homes.exceptions.NoReceivedInvitationException
import com.masahirosaito.spigot.homes.findOnlinePlayer
import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.ACCEPT_INVITATION_FROM
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.ACCEPT_INVITATION_TO
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.CANCEL_INVITATION_FROM
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.CANCEL_INVITATION_TO
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.USAGE_INVITE
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.USAGE_INVITE_PLAYER
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.USAGE_INVITE_PLAYER_NAME
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.concurrent.thread

class InviteCommand(override val plugin: Homes) : PlayerCommand {
    private val INVITE_META = "homes.invite"
    override val name: String = "invite"
    override val description: String = InviteCommandStrings.DESCRIPTION()
    override val permissions: List<String> = listOf(
            Permission.home_command
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home invite" to USAGE_INVITE(),
            "/home invite <player_name>" to USAGE_INVITE_PLAYER(),
            "/home invite <player_name> <home_name>" to USAGE_INVITE_PLAYER_NAME()
    ))
    override val commands: List<BaseCommand> = listOf(
            InvitePlayerCommand(this),
            InvitePlayerNameCommand(this)
    )

    override fun fee(): Double = plugin.fee.INVITE

    override fun configs(): List<Boolean> = listOf(
            Configs.onInvite
    )

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        if (!player.hasMetadata(INVITE_META)) {
            throw NoReceivedInvitationException()
        } else {
            val th = player.getMetadata(INVITE_META).first().value() as Thread
            if (th.isAlive) {
                th.interrupt()
                th.join()
            }
        }
    }

    fun inviteHome(homesEntity: HomesEntity, player: Player, playerName: String, message: String) {
        val op = findOnlinePlayer(playerName).apply {
            if (hasMetadata(INVITE_META)) {
                throw AlreadyHasInvitationException(this)
            }
            send(this, message)
        }
        val th = thread(name = "$INVITE_META.${player.name}.$playerName") {
            try {
                Thread.sleep(30000)
                val target = findOnlinePlayer(playerName)
                if (target.hasMetadata(INVITE_META)) {
                    target.removeMetadata(INVITE_META, plugin)
                    send(target, CANCEL_INVITATION_FROM(player.name))
                    send(player, CANCEL_INVITATION_TO(target.name))
                }
            } catch (e: InterruptedException) {
                try {
                    val target = findOnlinePlayer(playerName)
                    target.teleport(homesEntity.location)
                    target.removeMetadata(INVITE_META, plugin)
                    send(target, ACCEPT_INVITATION_FROM(homesEntity.offlinePlayer.name))
                    try {
                        val owner = findOnlinePlayer(homesEntity.offlinePlayer.uniqueId)
                        send(owner, ACCEPT_INVITATION_TO(target.name))
                    } catch(e: Exception) {
                    }
                } catch (e: Exception) {
                }
            } catch(e: Exception) {
                e.message?.let { send(player, it) }
            }
        }
        op.setMetadata(INVITE_META, FixedMetadataValue(plugin, th))
    }
}
