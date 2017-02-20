package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File

data class Configs(

        @SerializedName("デバッグメッセージを表示する")
        val onDebug: Boolean = false,

        @SerializedName("名前付きホームを設定可能にする")
        val onNamedHome: Boolean = true,

        @SerializedName("他の人のホームに移動可能にする")
        val onFriendHome: Boolean = false

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