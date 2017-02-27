package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File

data class Configs(

        @SerializedName("Allow showing debug messages")
        val onDebug: Boolean = false,

        @SerializedName("Allow using named home")
        val onNamedHome: Boolean = true,

        @SerializedName("Allow using player's home")
        val onFriendHome: Boolean = true,

        @SerializedName("Allow respawning default home")
        val onDefaultHomeRespawn: Boolean = true,

        @SerializedName("Allow checking update")
        val onUpdateCheck: Boolean = true,

        @SerializedName("Allow setting home private")
        val onPrivate: Boolean = true,

        @SerializedName("Allow invitation")
        val onInvite: Boolean = true,

        @SerializedName("The limit number of named home")
        val homeLimit: Int = -1

) {
    fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    companion object {
        fun load(file: File): Configs {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) Configs().toJson() else it
            }, Configs::class.java).apply { file.writeText(toJson()) }
        }
    }
}
