package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.homes.tests.commands.Permission
import com.masahirosaito.spigot.homes.tests.commands.SetCommandData
import com.masahirosaito.spigot.homes.tests.exceptions.CommandArgumentIncorrectException
import com.masahirosaito.spigot.homes.tests.exceptions.NotHavePermissionException
import com.masahirosaito.spigot.homes.tests.utils.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.junit.After
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
class SetCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var homeCommand: HomeCommand
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        homeCommand = pluginCommand.executor as HomeCommand
        logs = (mockServer.logger as SpyLogger).logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)

        nepian.set(Permission.HOME_DEFAULT)
        nepian.set(Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER)
        nepian.set(Permission.HOME_PLAYER_NAME)
    }

    @After
    fun tearDown() {
        logs.forEachIndexed { i, s -> println("$i -> $s") }
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コマンドの親権限を持っていない場合() {
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        assertEquals(SetCommandData.msg(NotHavePermissionException(Permission.HOME_SET)), logs.last())

        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        assertEquals(SetCommandData.msg(NotHavePermissionException(Permission.HOME_SET)), logs.last())
    }

    @Test
    fun 名前付きホーム設定の権限を持っていない場合() {
        nepian.set(Permission.HOME_SET)
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        assertEquals(SetCommandData.msg(NotHavePermissionException(Permission.HOME_SET_NAME)), logs.last())
    }

    @Test
    fun 引数が間違っている場合() {
        nepian.set(Permission.HOME_SET)
        nepian.set(Permission.HOME_SET_NAME)
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1", "home2"))
        assertEquals(SetCommandData.msg(CommandArgumentIncorrectException(SetCommandData)), logs.last())
    }

    @Test
    fun デフォルトホームの設定と移動() {
        nepian.set(Permission.HOME_SET)
        nepian.set(Permission.HOME_SET_NAME)
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        val firstLocation = nepian.location

        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        assertEquals(SetCommandData.msgSuccessSetDefaultHome(), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(firstLocation, nepian.location)
    }

    @Test
    fun 名前付きホームの設定と移動() {
        nepian.set(Permission.HOME_SET)
        nepian.set(Permission.HOME_SET_NAME)
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        val firstLocation = nepian.location

        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        assertEquals(SetCommandData.msgSuccessSetNamedHome("home1"), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(firstLocation, nepian.location)
    }
}
