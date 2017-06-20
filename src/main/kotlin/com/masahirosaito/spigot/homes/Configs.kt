package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.datas.ConfigData
import java.io.File

object Configs {

    var language: String = "en"

    var onDebug: Boolean = false

    var onNamedHome: Boolean = true

    var onFriendHome: Boolean = true

    var onDefaultHomeRespawn: Boolean = true

    var onUpdateCheck: Boolean = true

    var onPrivate: Boolean = true

    var onInvite: Boolean = true

    var homeLimit: Int = -1

    var onHomeDisplay: Boolean = true

    var teleportDelay: Int = 3

    var onMoveCancel: Boolean = true

    var onDamageCancel: Boolean = true

    fun load() {

        loadData(File(homes.dataFolder, "configs.json").load(), ConfigData::class.java).let {
            language = it.language
            onDebug = it.onDebug
            onNamedHome = it.onNamedHome
            onFriendHome = it.onFriendHome
            onDefaultHomeRespawn = it.onDefaultHomeRespawn
            onUpdateCheck = it.onUpdateCheck
            onPrivate = it.onPrivate
            onInvite = it.onInvite
            homeLimit = it.homeLimit
            onHomeDisplay = it.onHomeDisplay
            teleportDelay = it.teleportDelay
            onMoveCancel = it.onMoveCancel
            onDamageCancel = it.onDamageCancel
        }
    }
}
