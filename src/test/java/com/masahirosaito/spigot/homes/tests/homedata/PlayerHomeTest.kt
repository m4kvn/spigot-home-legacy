package com.masahirosaito.spigot.homes.tests.homedata

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import com.masahirosaito.spigot.homes.tests.utils.MockPlayerFactory
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
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
class PlayerHomeTest {
    lateinit var mockServer: Server
    lateinit var nepian: Player
    lateinit var playerHome: PlayerHome
    lateinit var homeData: HomeData

    val homeName = "default"

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        playerHome = PlayerHome()
        homeData = HomeData(nepian.uniqueId, homeName, LocationData.new(nepian.location))

        playerHome.defaultHomeData = homeData
        playerHome.namedHomeData.add(homeData)
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), CoreMatchers.`is`(true))
    }

    @Test
    fun findNamedHome() {
        assertThat(playerHome.findNamedHome(nepian, homeName), `is`(homeData))
    }

    @Test(expected = CanNotFindNamedHomeException::class)
    fun findNamedHomeException() {
        playerHome.namedHomeData.remove(homeData)
        playerHome.findNamedHome(nepian, homeName)
    }

    @Test
    fun findDefaultHome() {
        assertThat(playerHome.findDefaultHome(nepian), `is`(homeData))
    }

    @Test(expected = CanNotFindDefaultHomeException::class)
    fun findDefaultHomeException() {
        playerHome.defaultHomeData = null
        playerHome.findDefaultHome(nepian)
    }

    @Test
    fun removeNamedHome() {
        playerHome.removeNamedHome(nepian, homeName)
        assertThat(playerHome.namedHomeData.any { it.name == homeName }, `is`(false))
    }

    @Test(expected = CanNotFindNamedHomeException::class)
    fun removeNamedHomeException() {
        playerHome.namedHomeData.remove(homeData)
        playerHome.removeNamedHome(nepian, homeName)
    }

    @Test
    fun removeDefaultHome() {
        playerHome.removeDefaultHome(nepian)
        assertThat(playerHome.defaultHomeData, `is`(nullValue()))
    }

    @Test(expected = CanNotFindDefaultHomeException::class)
    fun removeDefaultHomeException() {
        playerHome.defaultHomeData = null
        playerHome.removeDefaultHome(nepian)
    }

    @Test
    fun haveName() {
        assertThat(playerHome.haveName(homeName), `is`(true))
        playerHome.namedHomeData.remove(homeData)
        assertThat(playerHome.haveName(homeName), `is`(false))
    }

    @Test
    fun setDefaultHome() {
        playerHome.defaultHomeData = null
        playerHome.setDefaultHome(nepian)
        assertThat(playerHome.defaultHomeData, `is`(notNullValue()))
    }

    @Test
    fun setNamedHome() {
        playerHome.setNamedHome(nepian, "home1", -1)
        assertThat(playerHome.namedHomeData.any { it.name == "home1" }, `is`(true))
    }

    @Test(expected = Exception::class)
    fun setNamedHomeException() {
        playerHome.setNamedHome(nepian, "home1", 0)
    }

    @Test
    fun copy() {
        assertThat(playerHome.copy(), `is`(playerHome))
    }

}
