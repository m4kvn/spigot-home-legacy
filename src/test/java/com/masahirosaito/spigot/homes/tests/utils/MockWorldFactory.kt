package com.masahirosaito.spigot.homes.tests.utils

import org.bukkit.Location
import org.bukkit.World
import org.powermock.api.mockito.PowerMockito
import java.util.*

object MockWorldFactory {
    val worlds: MutableMap<UUID, World> = mutableMapOf()
    val world: World = MockWorldFactory.makeNewMockWorld().apply { MockWorldFactory.worlds.put(uid, this) }

    fun makeNewMockWorld(): World {
        return PowerMockito.mock(World::class.java).apply {
            val uuid = UUID.randomUUID()
            PowerMockito.`when`(uid).thenReturn(uuid)
            MockWorldFactory.register(this)
        }
    }

    fun makeRandomLocation(): Location {
        return Random().run {
            Location(world, nextDouble(), nextDouble(), nextDouble(), nextFloat(), nextFloat())
        }
    }

    private fun register(world: World) {
        MockWorldFactory.worlds.put(world.uid, world)
    }

    fun clear() {
        MockWorldFactory.worlds.clear()
    }
}
