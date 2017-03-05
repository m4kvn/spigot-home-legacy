package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
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
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class HomesTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        logs = TestInstanceCreator.spyLogger.logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun 設定ファイルの生成() {
        assertTrue(File(TestInstanceCreator.pluginFolder, "configs.json").exists())
    }

    @Test
    fun データファイルの生成() {
        assertTrue(File(TestInstanceCreator.pluginFolder, "playerhomes.json").exists())
    }

    @Test
    fun コマンドの登録() {
        assertNotNull(homes.getCommand("home").executor)
    }
}
