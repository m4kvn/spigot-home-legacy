package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.findOnlinePlayer
import com.masahirosaito.spigot.homes.homedata.HomeData
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.concurrent.thread

class InviteCommand(override val plugin: Homes) : SubCommand {
    private val metadata = plugin.name + ".invite"

    override fun name(): String = "invite"

    override fun permission(): String = Permission.home_command_invite

    override fun description(): String = "Invite the other player to your home"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home invite" to "Accept an invitation",
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
        if (!player.hasMetadata(metadata)) throw Exception("You have not received an invitation")
        val homeData = player.getMetadata(metadata).first().value() as HomeData
        player.teleport(homeData.location())
        player.removeMetadata(metadata, plugin)
        send(player, "${ChatColor.AQUA}You accepted ${findOfflinePlayer(homeData.ownerUid).name}'s invitation")
        try {
            val owner = findOnlinePlayer(homeData.ownerUid)
            send(owner, "${ChatColor.AQUA}${player.name} accepted your invitation")
        } catch(e: Exception) {
        }
    }

    private fun inviteDefaultHome(player: Player, playerName: String) {
        val data = plugin.homeManager.findDefaultHome(player)
        inviteHome(data, player, playerName, getInviteMessage(player))
        send(player, getInvitedMessage(playerName))
    }

    private fun inviteNamedHome(player: Player, playerName: String, homeName: String) {
        checkConfig(plugin.configs.onNamedHome)
        checkPermission(player, Permission.home_command_invite_name)
        val data = plugin.homeManager.findNamedHome(player, homeName)
        inviteHome(data, player, playerName, getInviteMessage(player, homeName))
        send(player, getInvitedMessage(playerName, homeName))
    }

    private fun inviteHome(homeData: HomeData, player: Player, playerName: String, message: String) {
        findOnlinePlayer(playerName).let {
            it.setMetadata(metadata, FixedMetadataValue(plugin, homeData))
            send(it, message)
        }
        thread {
            Thread.sleep(30000)
            try {
                val target = findOnlinePlayer(playerName)
                if (target.hasMetadata(metadata)) {
                    target.removeMetadata(metadata, plugin)
                    send(target, buildString {
                        append("${ChatColor.RED}Invitation from ")
                        append("${ChatColor.RESET}${player.name}${ChatColor.RED} ")
                        append("has been canceled${ChatColor.RESET}")
                    })
                    send(player, buildString {
                        append("${ChatColor.RESET}${target.name}${ChatColor.RED} ")
                        append("canceled your invitation${ChatColor.RESET}")
                    })
                }
            } catch(e: Exception) {
            }
        }
    }

    private fun getInviteMessage(player: Player, homeName: String? = null) = buildString {
        val r = ChatColor.RESET; val y = ChatColor.YELLOW
        append("${y}You have been invited from $r${player.name}$y to ${player.name}'s ")
        append(if (homeName == null) "default home" else "home named $r$homeName$y.\n")
        append("To accept an invitation, please run ")
        append("${ChatColor.AQUA}/home invite$y within ${ChatColor.LIGHT_PURPLE}30 seconds$r")
    }

    private fun getInvitedMessage(playerName: String, homeName: String? = null) = buildString {
        append("${ChatColor.YELLOW}You invited ${ChatColor.RESET}$playerName${ChatColor.YELLOW} to your ")
        append(if (homeName == null) "default home" else "home named ${ChatColor.RESET}$homeName")
    }
}
