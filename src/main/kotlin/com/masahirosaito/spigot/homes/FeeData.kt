package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File

data class FeeData(

        @SerializedName("Home Command Fee")
        val HOME: Double = 0.0,

        @SerializedName("Home Name Command Fee")
        val HOME_NAME: Double = 0.0,

        @SerializedName("Home Player Command Fee")
        val HOME_PLAYER: Double = 0.0,

        @SerializedName("Home Name Player Command Fee")
        val HOME_NAME_PLAYER: Double = 0.0,

        @SerializedName("Set Command Fee")
        val SET: Double = 0.0,

        @SerializedName("Set Name Command Fee")
        val SET_NAME: Double = 0.0,

        @SerializedName("Delete Command Fee")
        val DELETE: Double = 0.0,

        @SerializedName("Delete Name Command Fee")
        val DELETE_NAME: Double = 0.0,

        @SerializedName("List Command Fee")
        val LIST: Double = 0.0,

        @SerializedName("List Player Command Fee")
        val LIST_PLAYER: Double = 0.0,

        @SerializedName("Help Command Fee")
        val HELP: Double = 0.0,

        @SerializedName("Help Usage Command Fee")
        val HELP_USAGE: Double = 0.0,

        @SerializedName("Private Command Fee")
        val PRIVATE: Double = 0.0,

        @SerializedName("Private Name Command Fee")
        val PRIVATE_NAME: Double = 0.0,

        @SerializedName("Invite Accept Command Fee")
        val INVITE: Double = 0.0,

        @SerializedName("Invite Player Command Fee")
        val INVITE_PLAYER: Double = 0.0,

        @SerializedName("Invite Player Name Command Fee")
        val INVITE_PLAYER_NAME: Double = 0.0
) {

    private fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): FeeData {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) FeeData().toJson() else it
            }, FeeData::class.java).apply { save(file)}
        }
    }
}
