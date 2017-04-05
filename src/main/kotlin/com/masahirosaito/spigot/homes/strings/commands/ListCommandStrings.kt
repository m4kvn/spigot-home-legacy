package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.datas.PlayerData
import com.masahirosaito.spigot.homes.datas.strings.commands.ListCommandStringData
import com.masahirosaito.spigot.homes.exceptions.NoHomeException
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import com.masahirosaito.spigot.homes.nms.HomesEntity
import java.io.File

object ListCommandStrings {
    lateinit private var data: ListCommandStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "list-command.json").load(), ListCommandStringData::class.java)
    }

    fun DESCRIPTION_PLAYER_COMMAND() =
            data.DESCRIPTION_PLAYER_COMMAND

    fun DESCRIPTION_CONSOLE_COMMAND() =
            data.DESCRIPTION_CONSOLE_COMMAND

    fun USAGE_CONSOLE_COMMAND_LIST() =
            data.USAGE_CONSOLE_COMMAND_LIST

    fun USAGE_PLAYER_COMMAND_LIST() =
            data.USAGE_PLAYER_COMMAND_LIST

    fun USAGE_LIST_PLAYER() =
            data.USAGE_LIST_PLAYER

    fun PLAYER_LIST() = buildString {
        append("Player List")
        PlayerDataManager.getPlayerDataList().forEach { (offlinePlayer, defaultHome, namedHomes) ->
            append("\n  &d${offlinePlayer.name}&r : ")
            if (defaultHome != null) append("default, ")
            append("named(&b${namedHomes.size}&r)")
        }
    }

    fun HOME_LIST(playerData: PlayerData, isPlayerHomeList: Boolean) = buildString {
        val offlinePlayer = playerData.offlinePlayer
        val defaultHome = playerData.defaultHome
        val namedHomes = playerData.namedHomes

        if (defaultHome == null && namedHomes.isEmpty()) {
            throw NoHomeException(offlinePlayer)
        }

        if (isPlayerHomeList
                .and(defaultHome != null && defaultHome.isPrivate)
                .and(namedHomes.all { it.isPrivate })) {
            throw NoHomeException(offlinePlayer)
        }

        append("${offlinePlayer.name}'s Home List")

        defaultHome?.let {
            if (!isPlayerHomeList || !it.isPrivate) {
                append("\n  [&6Default&r] ${getText(it)}")
            }
        }

        if (Configs.onNamedHome) {
            namedHomes.filter { !isPlayerHomeList || !it.isPrivate }.apply {
                if (isNotEmpty()) {
                    append("\n  [&6Named Home&r]\n")
                    this.forEach {
                        append("    &d${it.homeName}&r")
                        append(" : ${getText(it)}\n")
                    }
                }
            }
        }
    }

    private fun getText(homesEntity: HomesEntity): String {
        val loc = homesEntity.location
        return buildString {
            append("&a${loc.world.name}&r, ")
            append("{&b${loc.x.toInt()}&r, &b${loc.y.toInt()}&r, &b${loc.z.toInt()}&r}, ")
            append(if (homesEntity.isPrivate) "&ePRIVATE&r" else "&9PUBLIC&r")
        }
    }
}
