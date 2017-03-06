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
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        minene = MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)

        nepian.setOps()
        minene.setOps()

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Successfully set as default home"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Successfully set as home named <home1>"))
    }

    @After
    fun tearDown() {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }
        minene.logger.logs.forEachIndexed { i, s -> println("[Minene] $i -> $s") }

        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun 引数が間違っている場合に使い方を表示する() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private"))
        assertThat(nepian.lastMsg(), `is`(buildString {
            append("[Homes] The argument is incorrect\n")
            append("private command usage:\n")
            append("/home private (on/off) : Set your default home private or public\n")
            append("/home private (on/off) <home_name> : Set your named home private or public")
        }))

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "aaa"))
        assertThat(nepian.lastMsg(), `is`(buildString {
            append("[Homes] The argument is incorrect\n")
            append("private command usage:\n")
            append("/home private (on/off) : Set your default home private or public\n")
            append("/home private (on/off) <home_name> : Set your named home private or public")
        }))

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1", "home2"))
        assertThat(nepian.lastMsg(), `is`(buildString {
            append("[Homes] The argument is incorrect\n")
            append("private command usage:\n")
            append("/home private (on/off) : Set your default home private or public\n")
            append("/home private (on/off) <home_name> : Set your named home private or public")
        }))
    }

    @Test
    fun デフォルトホームのプライベート化には親権限が必要() {
        nepian.setOps(false)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command>"))
    }

    @Test
    fun 名前付きホームのプライベートには親権限が必要() {
        nepian.setOps(false)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command>"))
    }

    @Test
    fun デフォルトホームのプライベート化にはプライベート権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.private>"))
    }

    @Test
    fun 名前付きホームのプライベート化にはプライベート権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.private>"))
    }

    @Test
    fun 名前付きホームのプライベート化には名前付きプライベート権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PRIVATE)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.private.name>"))
    }

    @Test
    fun プライベート権限を持っている場合はデフォルトホームをプライベート化できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PRIVATE)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Set your default home PRIVATE"))
    }

    @Test
    fun 名前付きプライベート権限を持っている場合は名前付きホームをプライベート化できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PRIVATE, Permission.HOME_PRIVATE_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Set your home named home1 PRIVATE"))
    }

    @Test
    fun プライベート化機能が設定でオフの場合はデフォルトホームをプライベート化できない() {
        homes.configs.copy(onPrivate = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Not allowed by the configuration of this server"))
    }

    @Test
    fun プライベート化機能が設定でオフの場合は名前付きホームをプライベート化できない() {
        homes.configs.copy(onPrivate = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Not allowed by the configuration of this server"))
    }

    @Test
    fun 名前付きホーム機能が設定でオフの場合は名前付きホームをプライベート化できない() {
        homes.configs.copy(onNamedHome = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Not allowed by the configuration of this server"))
    }
}
