package com.masahirosaito.spigot.homes.testutils

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.mockito.Matchers.any
import org.powermock.api.mockito.PowerMockito.mock
import java.util.*
import org.powermock.api.mockito.PowerMockito.`when` as pwhen

object MockWorldFactory {
    val worlds: MutableMap<UUID, World> = mutableMapOf()

    fun makeNewMockWorld(name: String): World {
        return mock(World::class.java).apply {
            val uuid = UUID.randomUUID()
            pwhen(uid).thenReturn(uuid)
            pwhen(getName()).thenReturn(name)
            pwhen(getChunkAt(any(Location::class.java)))
                    .then { makeNewChunk() }
            MockWorldFactory.register(this)
        }
    }

    fun makeNewChunk(): Chunk {
        return mock(Chunk::class.java).apply {
            pwhen(isLoaded).thenReturn(false)
        }
    }

    fun makeRandomLocation() = Location(makeNewMockWorld("world"),
            randomDouble(), randomDouble(), randomDouble(),
            randomFloat(), randomFloat())

    private fun register(world: World) {
        MockWorldFactory.worlds.put(world.uid, world)
    }

    fun clear() {
        MockWorldFactory.worlds.clear()
    }

    private fun randomDouble(): Double = Random().nextDouble() * 100

    private fun randomFloat(): Float = Random().nextFloat() * 100
}
