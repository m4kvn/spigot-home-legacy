package com.masahirosaito.spigot.homes.strings.commands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.datas.PlayerData
import com.masahirosaito.spigot.homes.datas.strings.commands.ListCommandStringData
import com.masahirosaito.spigot.homes.exceptions.NoHomeException
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.nms.HomesEntity
import java.io.File

object ListCommandStrings {
    private const val FILE_NAME = "list-command.json"
    private lateinit var data: ListCommandStringData

    val DESCRIPTION_PLAYER_COMMAND: String get() = data.DESCRIPTION_PLAYER_COMMAND
    val DESCRIPTION_CONSOLE_COMMAND: String get() = data.DESCRIPTION_CONSOLE_COMMAND
    val USAGE_CONSOLE_COMMAND_LIST: String get() = data.USAGE_CONSOLE_COMMAND_LIST
    val USAGE_PLAYER_COMMAND_LIST: String get() = data.USAGE_PLAYER_COMMAND_LIST
    val USAGE_LIST_PLAYER: String get() = data.USAGE_LIST_PLAYER

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { ListCommandStringData() }
    }

    fun createPlayerListMessage() = buildString {
        append("Player List")
        PlayerDataManager.getPlayerDataList().forEach { (offlinePlayer, defaultHome, namedHomes) ->
            append("\n  &d${offlinePlayer.name}&r : ")
            if (defaultHome != null) append("default, ")
            append("named(&b${namedHomes.size}&r)")
        }
    }

    fun createHomeListMessage(playerData: PlayerData, isPlayerHomeList: Boolean) = buildString {
        val offlinePlayer = playerData.offlinePlayer
        val defaultHome = playerData.defaultHome
        val namedHomes = playerData.namedHomes

        if (defaultHome == null && namedHomes.isEmpty()) {
            throw NoHomeException(offlinePlayer)
        }

        if (isPlayerHomeList
                .and(defaultHome != null && defaultHome.isPrivate)
                .and(namedHomes.all { it.isPrivate })
        ) {
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
            append("&a${loc.world?.name}&r, ")
            append("{&b${loc.x.toInt()}&r, &b${loc.y.toInt()}&r, &b${loc.z.toInt()}&r}, ")
            append(if (homesEntity.isPrivate) "&ePRIVATE&r" else "&9PUBLIC&r")
        }
    }
}
