package com.masahirosaito.spigot.homes.tests.homedata

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.exceptions.PlayerHomeIsPrivateException
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.tests.utils.MockPlayerFactory
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
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class HomeDataTest {
    lateinit var mockServer: Server
    lateinit var nepian: Player
    lateinit var location: Location
    lateinit var locationData: LocationData
    lateinit var homeData: HomeData

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        location = nepian.location
        locationData = LocationData.new(location)
        homeData = HomeData(nepian.uniqueId, "default", locationData)
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun location() {
        assertThat(homeData.location(), `is`(location))
    }

    @Test(expected = PlayerHomeIsPrivateException::class)
    fun checkPrivate() {
        try {
            homeData.checkPrivate(nepian)
        } catch (e: Exception) {
            Assert.fail()
        }
        homeData.isPrivate = true
        homeData.checkPrivate(nepian)
    }

    @Test
    fun getOwnerUid() {
        assertThat(homeData.ownerUid, `is`(nepian.uniqueId))
    }

    @Test
    fun getName() {
        assertThat(homeData.name, `is`("default"))
    }

    @Test
    fun isPrivate() {
        assertThat(homeData.isPrivate, `is`(false))
    }

    @Test
    fun setPrivate() {
        homeData.isPrivate = true

        assertThat(homeData.isPrivate, `is`(true))
    }

    @Test
    fun copy() {
        assertThat(homeData.copy(), `is`(homeData))
    }
}
