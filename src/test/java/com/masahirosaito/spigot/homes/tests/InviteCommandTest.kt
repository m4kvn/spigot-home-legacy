package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.commands.SetCommandData
import com.masahirosaito.spigot.homes.tests.utils.*
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class InviteCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player

    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        logs = TestInstanceCreator.spyLogger.logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)

        nepian.set(Permission.HOME_DEFAULT, Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER, Permission.HOME_PLAYER_NAME)
        nepian.set(Permission.HOME_SET, Permission.HOME_SET_NAME)

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        assertEquals(SetCommandData.msgSuccessSetDefaultHome(), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        assertEquals(SetCommandData.msgSuccessSetNamedHome("home1"), logs.last())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }
}
