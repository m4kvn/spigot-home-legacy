package com.masahirosaito.spigot.homes.tests.homedata

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.tests.utils.MockWorldFactory
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class LocationDataTest {
    lateinit var location: Location
    lateinit var locationData: LocationData

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        location = MockWorldFactory.makeRandomLocation()
        locationData = LocationData(location.world.uid,
                location.x, location.y, location.z, location.yaw, location.pitch)
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun toLocation() {
        assertThat(locationData.toLocation(), `is`(location))
    }

    @Test
    fun getWorldUid() {
        assertThat(locationData.worldUid, `is`(location.world.uid))
    }

    @Test
    fun getX() {
        assertThat(locationData.x, `is`(location.x))
    }

    @Test
    fun getY() {
        assertThat(locationData.y, `is`(location.y))
    }

    @Test
    fun getZ() {
        assertThat(locationData.z, `is`(location.z))
    }

    @Test
    fun getYaw() {
        assertThat(locationData.yaw, `is`(location.yaw))
    }

    @Test
    fun getPitch() {
        assertThat(locationData.pitch, `is`(location.pitch))
    }

    @Test
    fun copy() {
        assertThat(locationData.copy(), `is`(locationData))
    }
}
