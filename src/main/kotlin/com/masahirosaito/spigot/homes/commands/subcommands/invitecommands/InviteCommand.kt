package com.masahirosaito.spigot.homes.commands.subcommands.invitecommands

import com.masahirosaito.spigot.homes.*
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.HomesException
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.nms.HomesEntity
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.concurrent.thread

class InviteCommand(override val plugin: Homes) : PlayerCommand {
    private val INVITE_META = "homes.invite"
    override val name: String = "invite"
    override val description: String = "Invite the other player to your home"
    override val permissions: List<String> = listOf(
            Permission.home_command
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home invite" to "Accept the invitation",
            "/home invite <player_name>" to "Invite to your default home",
            "/home invite <player_name> <home_name>" to "Invite to your named home"
    ))
    override val commands: List<BaseCommand> = listOf(
            InvitePlayerCommand(this),
            InvitePlayerNameCommand(this)
    )

    override fun fee(): Double = plugin.fee.INVITE

    override fun configs(): List<Boolean> = listOf(
            plugin.configs.onInvite
    )

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        if (!player.hasMetadata(INVITE_META)) {
            throw HomesException("You have not received an invitation")
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
                throw HomesException("$name already has another invitation")
            }
            send(this, message)
        }
        val th = thread(name = "$INVITE_META.${player.name}.$playerName") {
            try {
                Thread.sleep(30000)
                val target = findOnlinePlayer(playerName)
                if (target.hasMetadata(INVITE_META)) {
                    target.removeMetadata(INVITE_META, plugin)
                    send(target, msgCancelInvitationFrom(player.name))
                    send(player, msgCanceledInvitationTo(target.name))
                }
            } catch (e: InterruptedException) {
                try {
                    val target = findOnlinePlayer(playerName)
                    target.teleport(homesEntity.location)
                    target.removeMetadata(INVITE_META, plugin)
                    send(target, msgAcceptInvitationFrom(homesEntity.offlinePlayer.name))
                    try {
                        val owner = findOnlinePlayer(homesEntity.offlinePlayer.uniqueId)
                        send(owner, msgAcceptedInvitationTo(target.name))
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

    private fun msgCancelInvitationFrom(playerName: String) = buildString {
        append("${ChatColor.RED}Invitation from")
        append(" ${ChatColor.RESET}$playerName${ChatColor.RED}")
        append(" has been canceled${ChatColor.RESET}")
    }

    private fun msgCanceledInvitationTo(playerName: String) = buildString {
        append("${ChatColor.RESET}$playerName${ChatColor.RED}")
        append(" canceled your invitation${ChatColor.RESET}")
    }

    private fun msgAcceptInvitationFrom(playerName: String) = buildString {
        append("${ChatColor.AQUA}You accepted $playerName's invitation")
    }

    private fun msgAcceptedInvitationTo(playerName: String) = buildString {
        append("${ChatColor.AQUA}$playerName accepted your invitation")
    }
}
