package com.masahirosaito.spigot.homes.datas.strings.commands

data class InviteCommandStringData(
        val DESCRIPTION: String = "Invite the other player to your home",
        val USAGE_INVITE: String = "Accept the invitation",
        val USAGE_INVITE_PLAYER: String = "Invite to your default home",
        val USAGE_INVITE_PLAYER_NAME: String = "Invite to your named home",
        val CANCEL_INVITATION_FROM: String = "&cInvitation from &r[player-name]&c has been canceled&r",
        val CANCEL_INVITATION_TO: String = "&r[player-name]&c canceled your invitation&r",
        val ACCEPT_INVITATION_FROM: String = "&bYou accepted [player-name]'s invitation&r",
        val ACCEPT_INVITATION_TO: String = "&b[player-name] accepted your invitation&r",
        val RECEIVE_DEFAULT_HOME_INVITATION_FROM: String = "&eYou have been invited from &r[player-name]&e to default home.\nTo accept an invitation, please run &b/home invite&e within &d30 seconds&r",
        val SEND_DEFAULT_HOME_INVITATION_TO: String = "&eYou Invited &r[player-name]&e to your default home",
        val RECEIVE_NAMED_HOME_INVITATION_FROM: String = "&eYou have been invited from &r[player-name]&e  to home named <&r[home-name]&e>.\nTo accept an invitation, please run &b/home invite&e within &d30 seconds&r",
        val SEND_NAMED_HOME_INVITATION_TO: String = "&eYou invited &r[player-name]&e to your home named <&r[home-name]&e>&r"
)
