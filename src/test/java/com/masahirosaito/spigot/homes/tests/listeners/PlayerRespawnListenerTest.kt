package com.masahirosaito.spigot.homes.tests.listeners

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import com.masahirosaito.spigot.homes.listeners.PlayerRespawnListener
import com.masahirosaito.spigot.homes.tests.utils.MockPlayerFactory
import com.masahirosaito.spigot.homes.tests.utils.MockWorldFactory
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerRespawnEvent
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
class PlayerRespawnListenerTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var nepian: Player
    lateinit var playerRespawnListener: PlayerRespawnListener
    lateinit var playerRespawnEvent: PlayerRespawnEvent
    lateinit var defaultLocationData: LocationData
    lateinit var defaultHomeData: HomeData

    val firstRespawnLocation = MockWorldFactory.makeRandomLocation()

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        playerRespawnListener = PlayerRespawnListener(homes)
        playerRespawnEvent = PlayerRespawnEvent(nepian, firstRespawnLocation, false)
        defaultLocationData = LocationData.new(MockWorldFactory.makeRandomLocation())
        defaultHomeData = HomeData(nepian.uniqueId, "default", defaultLocationData)
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun イベントのリスポーン地点がデフォルトホームに設定されている() {

        homes.homeManager.playerHomes.put(nepian.uniqueId, PlayerHome(defaultHomeData))

        playerRespawnListener.onPlayerRespawn(playerRespawnEvent)

        assertThat(playerRespawnEvent.respawnLocation, `is`(defaultLocationData.toLocation()))
    }

    @Test
    fun デフォルトホームが設定されていない場合はイベントのリスポーン地点を変更しない() {

        playerRespawnListener.onPlayerRespawn(playerRespawnEvent)

        assertThat(playerRespawnEvent.respawnLocation, `is`(firstRespawnLocation))
    }

    @Test
    fun ホームリスポーン設定がオフの場合はイベントのリスポーン地点を変更しない() {

        homes.configs.copy(onDefaultHomeRespawn = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }

        playerRespawnListener.onPlayerRespawn(playerRespawnEvent)

        assertThat(playerRespawnEvent.respawnLocation, `is`(firstRespawnLocation))
    }
}
