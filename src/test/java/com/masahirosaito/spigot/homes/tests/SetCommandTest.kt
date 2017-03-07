package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
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
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
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
class SetCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var nepian: Player
    lateinit var minene: Player
    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        minene = MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)

        nepian.setOps()
        minene.setOps()
    }

    @After
    fun tearDown() {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }

        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun デフォルトホームの設定には親権限が必要() {
        nepian.setOps(false)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command>"))
    }

    @Test
    fun 名前付きホームの設定には親権限が必要() {
        nepian.setOps(false)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command>"))
    }

    @Test
    fun デフォルトホームの設定には設定権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.set>"))
    }

    @Test
    fun 名前付きホームの設定には設定権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.set>"))
    }

    @Test
    fun 名前付きホームの設定には名前付き設定権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_SET)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.set.name>"))
    }

    @Test
    fun 設定権限を持っている場合はデフォルトホームを設定できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_SET)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Successfully set as default home"))
    }

    @Test
    fun 名前付き設定権限を持っている場合は名前付きホームを設定できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_SET, Permission.HOME_SET_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Successfully set as home named <home1>"))
    }

    @Test
    fun 引数が間違っていた場合に使い方を表示する() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1", "home2"))

        assertThat(nepian.lastMsg(), `is`(buildString {
            append("[Homes] The argument is incorrect\n")
            append("set command usage:\n")
            append("/home set : Set your location to your default home\n")
            append("/home set <home_name> : Set your location to your named home")
        }))
    }

    @Test
    fun 名前付きホーム機能が設定でオフの場合は名前付きホームを設定できない() {
        homes.configs.copy(onNamedHome = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Not allowed by the configuration of this server"))
    }

    @Test
    fun 名前付きホームの制限設定が無制限の場合は名前付きホームをいくらでも設定できる() {
        homes.configs.copy(homeLimit = -1).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }
        repeat(1000, { i ->
            nepian.teleport(MockWorldFactory.makeRandomLocation())
            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home$i"))

            assertThat(nepian.lastMsg(), `is`("[Homes] Successfully set as home named <home$i>"))
        })
    }

    @Test
    fun 名前付きホームの制限設定が設定されている場合は名前付きホームの設定できる数が制限される() {
        homes.configs.copy(homeLimit = 5).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }
        repeat(6, { i ->
            nepian.teleport(MockWorldFactory.makeRandomLocation())
            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home$i"))
        })

        assertThat(nepian.lastMsg(), `is`("[Homes] You can not set more homes (Limit: 5)"))
    }

    @Test
    fun 設定したデフォルトホームへホームコマンドで移動できる() {
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)

        assertThat(nepian.location, `is`(defaultLocation))
    }

    @Test
    fun 設定した名前付きホームへ名前付きホームコマンドで移動できる() {
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))

        assertThat(nepian.location, `is`(namedLocation))
    }

    @Test
    fun デフォルトホームが既に設定されている場合は上書きされる() {
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)

        assertThat(nepian.location, `is`(not(defaultLocation)))
    }

    @Test
    fun 名前付きホームが既に設定されている場合は上書きされる() {
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))

        assertThat(nepian.location, `is`(not(namedLocation)))
    }

    @Test
    fun 設定されたデフォルトホームに他の人が移動できる() {
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))

        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 設定された名前付きホームに他の人が移動できる() {
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))

        assertThat(minene.location, `is`(namedLocation))
    }
}
