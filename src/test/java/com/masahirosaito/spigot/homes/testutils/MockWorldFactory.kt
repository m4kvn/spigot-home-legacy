package com.masahirosaito.spigot.homes.testutils

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import java.util.*
import org.mockito.Mockito.`when` as pwhen

object MockWorldFactory {
    val worlds: MutableMap<UUID, World> = mutableMapOf()

    private fun makeNewMockWorld(name: String = "world"): World {
        return mock(World::class.java).apply {
            val uuid = UUID.randomUUID()
            pwhen(uid).thenReturn(uuid)
            pwhen(getName()).thenReturn(name)
            pwhen(getChunkAt(any(Location::class.java)))
                .then { makeNewChunk() }
            register(this)
        }
    }

    private fun makeNewChunk(): Chunk {
        return mock(Chunk::class.java).apply {
            pwhen(isLoaded).thenReturn(false)
        }
    }

    fun makeRandomLocation() = Location(
        makeNewMockWorld(),
        randomDouble(), randomDouble(), randomDouble(),
        randomFloat(), randomFloat()
    )

    private fun register(world: World) {
        worlds[world.uid] = world
    }

    fun clear() {
        worlds.clear()
    }

    private fun randomDouble(): Double = Random().nextDouble() * 100

    private fun randomFloat(): Float = Random().nextFloat() * 100
}
