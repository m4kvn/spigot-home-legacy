package com.masahirosaito.spigot.homes.oldhomedata

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.util.*

data class OldHomeData(val playerHomes: MutableMap<UUID, OldPlayerHome> = mutableMapOf()) {

    fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): OldHomeData {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) OldHomeData().toJson() else it
            }, OldHomeData::class.java).apply { save(file)}
        }
    }
}
