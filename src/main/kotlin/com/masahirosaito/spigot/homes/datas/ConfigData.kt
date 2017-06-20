package com.masahirosaito.spigot.homes.datas

import com.google.gson.annotations.SerializedName
import com.masahirosaito.spigot.homes.Configs

data class ConfigData(

        @SerializedName("Language folder name")
        var language: String = "en",

        @SerializedName("Allow showing debug messages")
        var onDebug: Boolean = false,

        @SerializedName("Allow using named home")
        var onNamedHome: Boolean = true,

        @SerializedName("Allow using player home")
        var onFriendHome: Boolean = true,

        @SerializedName("Allow respawning default home")
        var onDefaultHomeRespawn: Boolean = true,

        @SerializedName("Allow checking update")
        var onUpdateCheck: Boolean = true,

        @SerializedName("Allow setting home private")
        var onPrivate: Boolean = true,

        @SerializedName("Allow invitation")
        var onInvite: Boolean = true,

        @SerializedName("The limit number of named home")
        var homeLimit: Int = -1,

        @SerializedName("Allow home display")
        var onHomeDisplay: Boolean = true,

        @SerializedName("Teleport delay seconds")
        var teleportDelay: Int = 3,

        @SerializedName("Cancel teleport when moved")
        var onMoveCancel: Boolean = true,

        @SerializedName("Cancel teleport when damaged")
        var onDamageCancel: Boolean = true

) {
    init {
        require(homeLimit >= -1)
        require(teleportDelay >= 0)
    }
}
