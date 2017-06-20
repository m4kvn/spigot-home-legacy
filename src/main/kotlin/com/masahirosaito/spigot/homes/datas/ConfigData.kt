package com.masahirosaito.spigot.homes.datas

import com.google.gson.annotations.SerializedName
import com.masahirosaito.spigot.homes.Configs

data class ConfigData(

        @SerializedName("Language folder name")
        var language: String = Configs.language,

        @SerializedName("Allow showing debug messages")
        var onDebug: Boolean = Configs.onDebug,

        @SerializedName("Allow using named home")
        var onNamedHome: Boolean = Configs.onNamedHome,

        @SerializedName("Allow using player home")
        var onFriendHome: Boolean = Configs.onFriendHome,

        @SerializedName("Allow respawning default home")
        var onDefaultHomeRespawn: Boolean = Configs.onDefaultHomeRespawn,

        @SerializedName("Allow checking update")
        var onUpdateCheck: Boolean = Configs.onUpdateCheck,

        @SerializedName("Allow setting home private")
        var onPrivate: Boolean = Configs.onPrivate,

        @SerializedName("Allow invitation")
        var onInvite: Boolean = Configs.onInvite,

        @SerializedName("The limit number of named home")
        var homeLimit: Int = Configs.homeLimit,

        @SerializedName("Allow home display")
        var onHomeDisplay: Boolean = Configs.onHomeDisplay,

        @SerializedName("Teleport delay seconds")
        var teleportDelay: Int = Configs.teleportDelay
) {
    init {
        require(homeLimit >= -1)
        require(teleportDelay >= 0)
    }
}
