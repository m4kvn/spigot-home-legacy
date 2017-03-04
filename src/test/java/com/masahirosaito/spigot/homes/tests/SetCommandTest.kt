package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.tests.utils.MockPlayerFactory
import com.masahirosaito.spigot.homes.tests.utils.MockWorldFactory
import com.masahirosaito.spigot.homes.tests.utils.SpyLogger
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
import org.junit.Assert
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
    lateinit var setCommand: SubCommand

    @Before
    fun setUp() {
        Assert.assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        homeCommand = pluginCommand.executor as HomeCommand
        logs = (mockServer.logger as SpyLogger).logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        setCommand = homeCommand.subCommands.find { it.name() == "set" } ?: throw Exception()
    }

    @After
    fun tearDown() {
        logs.forEachIndexed { i, s -> println("$i -> $s") }
        Assert.assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun デフォルトホームの設定と移動() {

    }

    @Test
    fun test() {
        // デフォルトホームの設定テスト
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        Assert.assertEquals(nepian.location, homes.homeManager.findDefaultHome(nepian).location())
        Assert.assertEquals(defaultHomeMsg, logs.last())

        // 名前付きホームの設定テスト
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        Assert.assertEquals(nepian.location, homes.homeManager.findNamedHome(nepian, "home1").location())
        Assert.assertEquals(namedHomeMsg("home1"), logs.last())

        // 引数を間違えた場合のテスト
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1", "home2"))
        Assert.assertEquals("$argumentIncorrectMsg\n$setCommandUsage", logs.last())

        // 設定したデフォルトホームへの移動テスト
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", null)
        Assert.assertEquals(homes.homeManager.findDefaultHome(nepian).location(), nepian.location)

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Nepian"))
        Assert.assertEquals(homes.homeManager.findDefaultHome(nepian).location(), nepian.location)

        // 設定した名前付きホームへの移動テスト
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        Assert.assertEquals(homes.homeManager.findNamedHome(nepian, "home1").location(), nepian.location)

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        homeCommand.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        Assert.assertEquals(homes.homeManager.findNamedHome(nepian, "home1").location(), nepian.location)

        // 他のプライヤーからの移動テスト
        MockPlayerFactory.makeNewMockPlayer("Minene", mockServer).apply {

            // Nepianのデフォルトホームへの移動
            teleport(MockWorldFactory.makeRandomLocation())
            homeCommand.onCommand(this, pluginCommand, "home", arrayOf("-p", "Nepian"))
            Assert.assertEquals(homes.homeManager.findDefaultHome(nepian).location(), this.location)

            // Nepianのhome1への移動
            teleport(MockWorldFactory.makeRandomLocation())
            homeCommand.onCommand(this, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            Assert.assertEquals(homes.homeManager.findNamedHome(nepian, "home1").location(), this.location)


        }
    }
}
