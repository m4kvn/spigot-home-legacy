package com.masahirosaito.spigot.homes.tests.utils

import org.bukkit.Location
import org.bukkit.World
import org.powermock.api.mockito.PowerMockito
import java.util.*

object MockWorldFactory {
    val worlds: MutableMap<UUID, World> = mutableMapOf()

    fun makeNewMockWorld(name: String): World {
        return PowerMockito.mock(World::class.java).apply {
            val uuid = UUID.randomUUID()
            PowerMockito.`when`(uid).thenReturn(uuid)
            PowerMockito.`when`(getName()).thenReturn(name)
            MockWorldFactory.register(this)
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
