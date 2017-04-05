package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.strings.Strings.PLAYER_NAME
import com.masahirosaito.spigot.homes.datas.strings.commands.InviteCommandStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import java.io.File

object InviteCommandStrings {
    lateinit private var data: InviteCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "invite-command.json").load(), InviteCommandStringData::class.java)
    }

    fun DESCRIPTION() =
            data.DESCRIPTION

    fun USAGE_INVITE() =
            data.USAGE_INVITE

    fun USAGE_INVITE_PLAYER() =
            data.USAGE_INVITE_PLAYER

    fun USAGE_INVITE_PLAYER_NAME() =
            data.USAGE_INVITE_PLAYER_NAME

    fun CANCEL_INVITATION_FROM(playerName: String) =
            data.CANCEL_INVITATION_FROM
                    .replace(PLAYER_NAME, playerName)

    fun CANCEL_INVITATION_TO(playerName: String) =
            data.CANCEL_INVITATION_TO
                    .replace(PLAYER_NAME, playerName)

    fun ACCEPT_INVITATION_FROM(playerName: String) =
            data.ACCEPT_INVITATION_FROM
                    .replace(PLAYER_NAME, playerName)

    fun ACCEPT_INVITATION_TO(playerName: String) =
            data.ACCEPT_INVITATION_TO
                    .replace(PLAYER_NAME, playerName)

    fun RECEIVE_DEFAULT_HOME_INVITATION_FROM(playerName: String) =
            data.RECEIVE_DEFAULT_HOME_INVITATION_FROM
                    .replace(PLAYER_NAME, playerName)

    fun SEND_DEFAULT_HOME_INVITATION_TO(playerName: String) =
            data.SEND_DEFAULT_HOME_INVITATION_TO
                    .replace(PLAYER_NAME, playerName)

    fun RECEIVE_NAMED_HOME_INVITATION_FROM(playerName: String, homeName: String) =
            data.RECEIVE_NAMED_HOME_INVITATION_FROM
                    .replace(PLAYER_NAME, playerName)
                    .replace(HOME_NAME, homeName)

    fun SEND_NAMED_HOME_INVITATION_TO(playerName: String, homeName: String) =
            data.SEND_NAMED_HOME_INVITATION_TO
                    .replace(PLAYER_NAME, playerName)
                    .replace(HOME_NAME, homeName)
}
