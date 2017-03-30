package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File

data class Configs(

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
        var homeLimit: Int = -1

) {
    fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): Configs {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) Configs().toJson() else it
            }, Configs::class.java).apply { save(file) }
        }
    }
}
