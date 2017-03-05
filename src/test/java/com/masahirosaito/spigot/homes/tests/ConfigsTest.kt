package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.utils.MockPlayerFactory
import com.masahirosaito.spigot.homes.tests.utils.SpyLogger
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.configFile
import com.masahirosaito.spigot.homes.tests.utils.setOps
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class ConfigsTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player

    @Before
    fun setUp() {
        Assert.assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        logs = (mockServer.logger as SpyLogger).logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)

        nepian.setOps()
        assertTrue(configFile.exists())
    }

    @After
    fun tearDown() {
        logs.forEachIndexed { i, s -> println("$i -> $s") }
        Assert.assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun 設定を変更したファイルの読み込み() {
        val offConfig = homes.configs.copy(
                onDebug = false,
                onNamedHome = false,
                onFriendHome = false,
                onDefaultHomeRespawn = false,
                onUpdateCheck = false,
                onPrivate = false,
                onInvite = false,
                homeLimit = 10
        )
        offConfig.save(configFile)
        homes.onDisable()
        homes.onEnable()
        assertEquals(offConfig, homes.configs)
    }
}
