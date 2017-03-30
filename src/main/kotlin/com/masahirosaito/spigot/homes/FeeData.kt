package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File

data class FeeData(

        @SerializedName("Home Command Fee")
        var HOME: Double = 0.0,

        @SerializedName("Home Name Command Fee")
        var HOME_NAME: Double = 0.0,

        @SerializedName("Home Player Command Fee")
        var HOME_PLAYER: Double = 0.0,

        @SerializedName("Home Name Player Command Fee")
        var HOME_NAME_PLAYER: Double = 0.0,

        @SerializedName("Set Command Fee")
        var SET: Double = 0.0,

        @SerializedName("Set Name Command Fee")
        var SET_NAME: Double = 0.0,

        @SerializedName("Delete Command Fee")
        var DELETE: Double = 0.0,

        @SerializedName("Delete Name Command Fee")
        var DELETE_NAME: Double = 0.0,

        @SerializedName("List Command Fee")
        var LIST: Double = 0.0,

        @SerializedName("List Player Command Fee")
        var LIST_PLAYER: Double = 0.0,

        @SerializedName("Help Command Fee")
        var HELP: Double = 0.0,

        @SerializedName("Help Usage Command Fee")
        var HELP_USAGE: Double = 0.0,

        @SerializedName("Private Command Fee")
        var PRIVATE: Double = 0.0,

        @SerializedName("Private Name Command Fee")
        var PRIVATE_NAME: Double = 0.0,

        @SerializedName("Invite Accept Command Fee")
        var INVITE: Double = 0.0,

        @SerializedName("Invite Player Command Fee")
        var INVITE_PLAYER: Double = 0.0,

        @SerializedName("Invite Player Name Command Fee")
        var INVITE_PLAYER_NAME: Double = 0.0
) {

    private fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): FeeData {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) FeeData().toJson() else it
            }, FeeData::class.java).apply { save(file) }
        }
    }
}
