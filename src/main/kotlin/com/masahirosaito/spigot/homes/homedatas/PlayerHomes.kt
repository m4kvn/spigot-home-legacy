package com.masahirosaito.spigot.homes.homedatas

import com.google.gson.Gson
import com.google.gson.GsonBuilder

data class PlayerHomes(
        val defaultHome: DefaultHome = DefaultHome(),
        val namedHomes: MutableSet<NamedHome> = mutableSetOf()
) {
    fun toJson(): String = GsonBuilder().create().toJson(PlayerHomesGson.fromPlayerHomes(this))

    companion object {
        fun fromJson(json: String): PlayerHomes {
            return Gson().fromJson(json, PlayerHomesGson::class.java).toOrigin()
        }

        private fun fromDummy(playerHomesGson: PlayerHomesGson): PlayerHomes {
            return PlayerHomes(playerHomesGson.defaultHome, playerHomesGson.namedHomes.toMutableSet())
        }

        private data class PlayerHomesGson(
                val defaultHome: DefaultHome = DefaultHome(),
                val namedHomes: Set<NamedHome> = emptySet()
        ) {
            fun toOrigin(): PlayerHomes {
                return PlayerHomes.fromDummy(this)
            }

            companion object {
                fun fromPlayerHomes(playerHomes: PlayerHomes): PlayerHomesGson {
                    return PlayerHomesGson(playerHomes.defaultHome, playerHomes.namedHomes)
                }
            }
        }
    }
}