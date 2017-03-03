package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.homes.utils.MockPlayerFactory
import com.masahirosaito.spigot.homes.utils.SpyLogger
import com.masahirosaito.spigot.homes.utils.TestInstanceCreator
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.junit.After
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

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun onEnableTest() {
        assertTrue(File(TestInstanceCreator.pluginFolder, "configs.json").exists())
        assertTrue(File(TestInstanceCreator.pluginFolder, "playerhomes.json").exists())
        assertTrue(homes.getCommand("home").executor is HomeCommand)
    }

    @Test
    fun onHomeCommandTest() {
        val pluginCommand = homes.getCommand("home")
        val homeCommand = pluginCommand.executor as HomeCommand
        val nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        val logs = (nepian.server.logger as SpyLogger).logs

        MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)

        pluginCommand.apply {
            executor.onCommand(nepian, this, "home", null)
            assertTrue(logs.last() == "[Homes] §cNepian's default home does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("home1"))
            assertTrue(logs.last() == "[Homes] §cNepian's named home <home1> does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("-p", "Nepian"))
            assertTrue(logs.last() == "[Homes] §cNepian's default home does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("-p", "Minene"))
            assertTrue(logs.last() == "[Homes] §cMinene's default home does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("-p", "Moichi"))
            assertTrue(logs.last() == "[Homes] §cPlayer <Moichi> does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("home1", "-p", "Nepian"))
            assertTrue(logs.last() == "[Homes] §cNepian's named home <home1> does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("home1", "-p", "Minene"))
            assertTrue(logs.last() == "[Homes] §cMinene's named home <home1> does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("home1", "-p", "Moichi"))
            assertTrue(logs.last() == "[Homes] §cPlayer <Moichi> does not exist§r")

            executor.onCommand(nepian, this, "home", arrayOf("-p"))
            assertTrue(logs.last() == "[Homes] §cThe argument is incorrect\n${homeCommand.usage()}§r")

            executor.onCommand(nepian, this, "home", arrayOf("home1", "-p"))
            assertTrue(logs.last() == "[Homes] §cThe argument is incorrect\n${homeCommand.usage()}§r")

            executor.onCommand(nepian, this, "home", arrayOf("home1", "home2"))
            assertTrue(logs.last() == "[Homes] §cThe argument is incorrect\n${homeCommand.usage()}§r")
        }

        logs.forEachIndexed { i, s -> println("$i -> $s") }
    }
}
