package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.defaultLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.cancelTeleport
import com.masahirosaito.spigot.homes.testutils.getDelayThread
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.java.JavaPluginLoader
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class,
        PluginManager::class, ServicesManager::class, RegisteredServiceProvider::class)
class DelayTeleporterTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun run() {
        DelayTeleporter.run(nepian, defaultLocation)
        nepian.getDelayThread()?.let {
            assertTrue(nepian.hasMetadata("homes.delay"))
            assertTrue(it.isAlive)
            it.join()
        }
        nepian.cancelTeleport()
    }

    @Test
    fun isAlreadyRun() {
        DelayTeleporter.run(nepian, defaultLocation)
        assertTrue(DelayTeleporter.isAlreadyRun(nepian))
        nepian.cancelTeleport()
    }

    @Test
    fun cancelTeleport() {
        DelayTeleporter.run(nepian, defaultLocation)
        nepian.getDelayThread()?.let {
            DelayTeleporter.cancelTeleport(nepian)
            it.join()
            assertFalse(it.isAlive)
            assertFalse(nepian.hasMetadata("homes.delay"))
        }
    }

}
