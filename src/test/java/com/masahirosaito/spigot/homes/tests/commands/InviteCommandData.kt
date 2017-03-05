package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.OfflinePlayer

object InviteCommandData : CommandData {

    override fun name(): String = "invite"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home invite" to "Accept the invitation",
            "/home invite <player_name>" to "Invite to your default home",
            "/home invite <player_name> <home_name>" to "Invite to your named home"
    )

    override fun description(): String = "Invite the other player to your home"

    fun metadata(homes: Homes) = "${homes.name}.invite"

    fun msgNoReceivedInvitation() = "You have not received an invitation"

    fun msgAlreadyReceivedInvitation(player: OfflinePlayer) = "${player.name} already has another invitation"

    fun msgReceivedInvitationFrom(player: OfflinePlayer, homeName: String? = null) = buildString {
        append("You have been invited from ${player.name} to ${player.name}'s ")
        append(if (homeName == null) "default home" else "home named $homeName.\n")
        append("To accept an invitation, please run /home invite within 30 seconds")
    }

    fun msgInvited(player: OfflinePlayer, homeName: String? = null) = buildString {
        append("You invited ${player.name}$ to your ")
        append(if (homeName == null) "default home" else "home named $homeName")
    }

    fun msgAcceptedInvitationFrom(player: OfflinePlayer) = "You accepted ${player.name}'s invitation"

    fun msgAcceptedInvitation(player: OfflinePlayer) = "${player.name} accepted your invitation"

    fun msgCanceledInvitationFrom(player: OfflinePlayer) = "Invitation from ${player.name} has been canceled"

    fun msgCanceledInvitation(target: OfflinePlayer) = "${target.name} canceled your invitation"
}
