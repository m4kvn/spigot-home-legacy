package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.HomeManager
import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.exceptions.CanNotFindDefaultHomeException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindNamedHomeException
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
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
import org.hamcrest.CoreMatchers.notNullValue
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
class HomeManagerTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var nepian: Player
    lateinit var homeManager: HomeManager

    lateinit var locationData: LocationData
    lateinit var homeData: HomeData

    val homeName = "default"
    val location = MockWorldFactory.makeRandomLocation()

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        homeManager = HomeManager()

        locationData = LocationData.new(location)
        homeData = HomeData(nepian.uniqueId, homeName, locationData)
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun プレイヤーホームが存在しない場合は新しく追加してそれを返す() {
        assertThat(homeManager.findPlayerHome(nepian), `is`(notNullValue()))
    }

    @Test
    fun プレイヤーホームが存在する場合はそれを返す() {
        homeManager.playerHomes.put(nepian.uniqueId, PlayerHome())
        assertThat(homeManager.findPlayerHome(nepian), `is`(notNullValue()))
    }

    @Test
    fun デフォルトホームが存在する場合はそれを返す() {
        homeManager.playerHomes.put(nepian.uniqueId, PlayerHome(homeData))
        assertThat(homeManager.findDefaultHome(nepian), `is`(homeData))
    }

    @Test(expected = CanNotFindDefaultHomeException::class)
    fun デフォルトホームが存在しない場合はエラーを返す() {
        homeManager.findDefaultHome(nepian)
    }

    @Test
    fun 名前付きホームが存在する場合はそれを返す() {
        homeManager.playerHomes.put(nepian.uniqueId, PlayerHome(namedHomeData = mutableListOf(homeData)))
        assertThat(homeManager.findNamedHome(nepian, homeName), `is`(homeData))
    }

    @Test(expected = CanNotFindNamedHomeException::class)
    fun 名前付きホームが存在しない場合はエラーを返す() {
        homeManager.findNamedHome(nepian, homeName)
    }

    @Test
    fun データをファイルに保存して読み込める() {
        homeManager.playerHomes.put(nepian.uniqueId, PlayerHome(homeData, mutableListOf(homeData)))
        homeManager.save(TestInstanceCreator.playerhomesFile)
        assertThat(HomeManager.load(TestInstanceCreator.playerhomesFile), `is`(homeManager))
    }
}
