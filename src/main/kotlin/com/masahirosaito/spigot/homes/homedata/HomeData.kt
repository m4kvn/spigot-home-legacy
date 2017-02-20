package com.masahirosaito.spigot.homes.homedata

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import java.io.File
import java.util.*

data class HomeData(val playerHomes: MutableMap<UUID, PlayerHome> = mutableMapOf()) {

    fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): HomeData {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) HomeData().toJson() else it
            }, HomeData::class.java).apply { save(file)}
        }
    }
}