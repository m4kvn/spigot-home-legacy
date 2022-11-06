package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.datas.strings.commands.InviteCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.strings.Strings.PLAYER_NAME
import java.io.File

object InviteCommandStrings {
    private const val FILE_NAME = "invite-command.json"
    private lateinit var data: InviteCommandStringData

    val DESCRIPTION: String get() = data.DESCRIPTION
    val USAGE_INVITE: String get() = data.USAGE_INVITE
    val USAGE_INVITE_PLAYER: String get() = data.USAGE_INVITE_PLAYER
    val USAGE_INVITE_PLAYER_NAME: String get() = data.USAGE_INVITE_PLAYER_NAME

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { InviteCommandStringData() }
    }

    fun createCancelInvitationFrom(playerName: String) =
        data.CANCEL_INVITATION_FROM
            .replace(PLAYER_NAME, playerName)

    fun createCancelInvitationTo(playerName: String) =
        data.CANCEL_INVITATION_TO
            .replace(PLAYER_NAME, playerName)

    fun createAcceptInvitationFrom(playerName: String) =
        data.ACCEPT_INVITATION_FROM
            .replace(PLAYER_NAME, playerName)

    fun createAcceptInvitationTo(playerName: String) =
        data.ACCEPT_INVITATION_TO
            .replace(PLAYER_NAME, playerName)

    fun createReceiveDefaultHomeInvitationFrom(playerName: String) =
        data.RECEIVE_DEFAULT_HOME_INVITATION_FROM
            .replace(PLAYER_NAME, playerName)

    fun createSendDefaultHomeInvitationTo(playerName: String) =
        data.SEND_DEFAULT_HOME_INVITATION_TO
            .replace(PLAYER_NAME, playerName)

    fun createReceiveNamedHomeInvitationFrom(playerName: String, homeName: String) =
        data.RECEIVE_NAMED_HOME_INVITATION_FROM
            .replace(PLAYER_NAME, playerName)
            .replace(HOME_NAME, homeName)

    fun createSendNamedHomeInvitationTo(playerName: String, homeName: String) =
        data.SEND_NAMED_HOME_INVITATION_TO
            .replace(PLAYER_NAME, playerName)
            .replace(HOME_NAME, homeName)
}
