package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.findOnlinePlayer
import com.masahirosaito.spigot.homes.homedata.HomeData
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.concurrent.thread

class InviteCommand(override val plugin: Homes) : SubCommand {
    private val meta = "homes.invite"

    override fun name(): String = "invite"

    override fun permission(): String = ""

    override fun description(): String = "Invite the other player to your home"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home invite" to "Accept the invitation",
            "/home invite <player_name>" to "Invite to your default home",
            "/home invite <player_name> <home_name>" to "Invite to your named home"
    )

    override fun configs(): List<Boolean> = listOf(plugin.configs.onInvite)

    override fun isInValidArgs(args: List<String>): Boolean = args.size > 2

    override fun execute(player: Player, args: List<String>) {
        when (args.size) {
            0 -> allowInvitation(player)
            1 -> inviteDefaultHome(player, args.first())
            2 -> inviteNamedHome(player, args.first(), args.last())
        }
    }

    private fun allowInvitation(player: Player) {
        if (!player.hasMetadata(meta)) {
            throw Exception("You have not received an invitation")
        } else {
            val th = player.getMetadata(meta).first().value() as Thread
            if (th.isAlive) {
                th.interrupt()
                th.join()
            }
        }
    }

    private fun inviteDefaultHome(player: Player, playerName: String) {
        checkPermission(player, Permission.home_command_invite)
        val data = plugin.homeManager.findDefaultHome(player)
        inviteHome(data, player, playerName, msgReceiveInvitationFrom(player.name))
        send(player, msgInvite(playerName))
    }

    private fun inviteNamedHome(player: Player, playerName: String, homeName: String) {
        checkConfig(plugin.configs.onNamedHome)
        checkPermission(player, Permission.home_command_invite)
        checkPermission(player, Permission.home_command_invite_name)
        val data = plugin.homeManager.findNamedHome(player, homeName)
        inviteHome(data, player, playerName, msgReceiveInvitationFrom(player.name, homeName))
        send(player, msgInvite(playerName, homeName))
    }

    private fun inviteHome(homeData: HomeData, player: Player, playerName: String, message: String) {
        val op = findOnlinePlayer(playerName).apply {
            if (hasMetadata(meta)) {
                throw Exception("$name already has another invitation")
            }
            send(this, message)
        }
        val th = thread(name = "$meta.${player.name}.$playerName") {
            try {
                Thread.sleep(30000)
                val target = findOnlinePlayer(playerName)
                if (target.hasMetadata(meta)) {
                    target.removeMetadata(meta, plugin)
                    send(target, msgCancelInvitationFrom(player.name))
                    send(player, msgCanceledInvitationTo(target.name))
                }
            } catch (e: InterruptedException) {
                try {
                    val target = findOnlinePlayer(playerName)
                    target.teleport(homeData.location())
                    target.removeMetadata(meta, plugin)
                    send(target, msgAcceptInvitationFrom(findOfflinePlayer(homeData.ownerUid).name))
                    try {
                        val owner = findOnlinePlayer(homeData.ownerUid)
                        send(owner, msgAcceptedInvitationTo(target.name))
                    } catch(e: Exception) {
                    }
                } catch (e: Exception) {
                }
            } catch(e: Exception) {
                e.message?.let { send(player, it) }
            }
        }
        op.setMetadata(meta, FixedMetadataValue(plugin, th))
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

    private fun msgReceiveInvitationFrom(playerName: String) = buildString {
        append("${ChatColor.YELLOW}You have been invited from")
        append(" ${ChatColor.RESET}$playerName${ChatColor.YELLOW} to default home.\n")
        append("To accept an invitation, please run ${ChatColor.AQUA}/home invite")
        append(" ${ChatColor.YELLOW}within ${ChatColor.LIGHT_PURPLE}30 seconds${ChatColor.RESET}")
    }

    private fun msgReceiveInvitationFrom(playerName: String, homeName: String) = buildString {
        append("${ChatColor.YELLOW}You have been invited from")
        append(" ${ChatColor.RESET}$playerName${ChatColor.YELLOW}")
        append(" to home named <${ChatColor.RESET}$homeName${ChatColor.YELLOW}>.\n")
        append("To accept an invitation, please run ${ChatColor.AQUA}/home invite")
        append(" ${ChatColor.YELLOW}within ${ChatColor.LIGHT_PURPLE}30 seconds${ChatColor.RESET}")
    }

    private fun msgInvite(playerName: String) = buildString {
        append("${ChatColor.YELLOW}You invited")
        append(" ${ChatColor.RESET}$playerName${ChatColor.YELLOW}")
        append(" to your default home")
    }

    private fun msgInvite(playerName: String, homeName: String) = buildString {
        append("${ChatColor.YELLOW}You invited ${ChatColor.RESET}$playerName${ChatColor.YELLOW}")
        append(" to your home named <${ChatColor.RESET}$homeName${ChatColor.YELLOW}>${ChatColor.RESET}")
    }
}
