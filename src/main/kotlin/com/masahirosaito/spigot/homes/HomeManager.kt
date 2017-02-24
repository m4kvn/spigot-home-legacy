package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import java.io.File
import java.util.*

data class HomeManager(val playerHomes: MutableMap<UUID, PlayerHome> = mutableMapOf()) {

    fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): HomeManager {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) HomeManager().toJson() else it
            }, HomeManager::class.java).apply { save(file)}
        }
    }
}
