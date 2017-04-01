package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.datas.ConfigData
import java.io.File

object Configs {

    var onDebug: Boolean = false

    var onNamedHome: Boolean = true

    var onFriendHome: Boolean = true

    var onDefaultHomeRespawn: Boolean = true

    var onUpdateCheck: Boolean = true

    var onPrivate: Boolean = true

    var onInvite: Boolean = true

    var homeLimit: Int = -1

    var onHomeDisplay: Boolean = true

    fun load(homes: Homes) {
        ConfigData.load(File(homes.dataFolder, "configs.json").load()).let {
            onDebug = it.onDebug
            onNamedHome = it.onNamedHome
            onFriendHome = it.onFriendHome
            onDefaultHomeRespawn = it.onDefaultHomeRespawn
            onUpdateCheck = it.onUpdateCheck
            onPrivate = it.onPrivate
            onInvite = it.onInvite
            homeLimit = it.homeLimit
            onHomeDisplay = it.onHomeDisplay
        }
    }
}
