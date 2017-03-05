package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.commands.PrivateCommandData
import com.masahirosaito.spigot.homes.tests.commands.SetCommandData
import com.masahirosaito.spigot.homes.tests.exceptions.NotHavePermissionException
import com.masahirosaito.spigot.homes.tests.utils.MockPlayerFactory
import com.masahirosaito.spigot.homes.tests.utils.MockWorldFactory
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.set
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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class PrivateCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player
    lateinit var minene: Player

    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location

    @Before
    fun setUp() {
        Assert.assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        logs = TestInstanceCreator.spyLogger.logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        minene = MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)

        nepian.set(Permission.HOME_DEFAULT, Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER, Permission.HOME_PLAYER_NAME)
        nepian.set(Permission.HOME_SET, Permission.HOME_SET_NAME)

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        Assert.assertEquals(SetCommandData.msgSuccessSetDefaultHome(), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        Assert.assertEquals(SetCommandData.msgSuccessSetNamedHome("home1"), logs.last())

        minene.teleport(MockWorldFactory.makeRandomLocation())
    }

    @After
    fun tearDown() {
        Assert.assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun 親権限を持っていない場合は全ての機能を実行できない() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        Assert.assertEquals(PrivateCommandData.msg(NotHavePermissionException(Permission.HOME_PRIVATE)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off"))
        Assert.assertEquals(PrivateCommandData.msg(NotHavePermissionException(Permission.HOME_PRIVATE)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        Assert.assertEquals(PrivateCommandData.msg(NotHavePermissionException(Permission.HOME_PRIVATE)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off", "home1"))
        Assert.assertEquals(PrivateCommandData.msg(NotHavePermissionException(Permission.HOME_PRIVATE)), logs.last())
    }
}
