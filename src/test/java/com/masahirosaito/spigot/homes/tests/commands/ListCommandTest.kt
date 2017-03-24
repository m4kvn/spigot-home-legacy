package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import com.masahirosaito.spigot.homes.tests.Permission
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
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class ListCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var nepian: Player
    lateinit var minene: Player

    @Before
    fun setUp() {
        Assert.assertThat(TestInstanceCreator.setUp(), CoreMatchers.`is`(true))

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

        Assert.assertThat(TestInstanceCreator.tearDown(), CoreMatchers.`is`(true))
    }

    @Test
    fun 引数が間違っている場合は使い方を表示し終わる() {
        buildString {
            append("[Homes] The argument is incorrect\n")
            append("list command usage:\n")
            append("/home list : Display the list of homes\n")
            append("/home list <player_name> : Display the list of player's homes")
        }.apply {
            command.onCommand(nepian, pluginCommand, "home", arrayOf("list", "Nepian", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun コマンドの実行には親権限が必要() {
        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("list", "Nepian"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun リストコマンドの実行にはリスト権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.list>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun リストプレイヤーコマンドの実行にはリストプレイヤー権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.list.player>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("list", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun リスト権限を持っている場合はリストコマンドでホームのリストを表示できる() {
        val playerHome = PlayerHome().apply {
            setDefaultHome(nepian.apply { teleport(MockWorldFactory.makeRandomLocation()) })
            setNamedHome(nepian, "home1", -1)
            setNamedHome(nepian, "home2", -1)
            setNamedHome(nepian, "home3", -1)
        }
        homes.homeManager.playerHomes.put(nepian.uniqueId, playerHome)
        nepian.set(Permission.HOME, Permission.HOME_LIST)
        nepian.setOps(false)

        buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PUBLIC\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
            append("    home2 : world, {0, 0, 0}, PUBLIC\n")
            append("    home3 : world, {0, 0, 0}, PUBLIC\n")
        }.apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun リストプレイヤー権限を持っている場合はリストプレイヤーコマンドでホームのリストを表示できる() {
        val playerHome = PlayerHome().apply {
            setDefaultHome(nepian.apply { teleport(MockWorldFactory.makeRandomLocation()) })
            setNamedHome(nepian, "home1", -1)
            setNamedHome(nepian, "home2", -1)
            setNamedHome(nepian, "home3", -1)
        }
        homes.homeManager.playerHomes.put(nepian.uniqueId, playerHome)
        minene.set(Permission.HOME, Permission.HOME_LIST_PLAYER)
        minene.setOps(false)

        buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PUBLIC\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
            append("    home2 : world, {0, 0, 0}, PUBLIC\n")
            append("    home3 : world, {0, 0, 0}, PUBLIC\n")
        }.apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 自分のホームリストの表示はプライベートホームも表示する() {
        val playerHome = PlayerHome().apply {
            setDefaultHome(nepian.apply { teleport(MockWorldFactory.makeRandomLocation()) })
            setNamedHome(nepian, "home1", -1)
            setNamedHome(nepian, "home2", -1)
            setNamedHome(nepian, "home3", -1)
            findDefaultHome(nepian).isPrivate = true
            findNamedHome(nepian, "home3").isPrivate = true
        }
        homes.homeManager.playerHomes.put(nepian.uniqueId, playerHome)

        buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PRIVATE\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
            append("    home2 : world, {0, 0, 0}, PUBLIC\n")
            append("    home3 : world, {0, 0, 0}, PRIVATE\n")
        }.apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
            assertThat(nepian.lastMsg(), `is`(this))
        }

        playerHome.removeDefaultHome(nepian)

        buildString {
            append("[Homes] Home List\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
            append("    home2 : world, {0, 0, 0}, PUBLIC\n")
            append("    home3 : world, {0, 0, 0}, PRIVATE\n")
        }.apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 他人のホームリストの表示にプレイベートホームは含めない() {
        val playerHome = PlayerHome().apply {
            setDefaultHome(nepian.apply { teleport(MockWorldFactory.makeRandomLocation()) })
            setNamedHome(nepian, "home1", -1)
            setNamedHome(nepian, "home2", -1)
            setNamedHome(nepian, "home3", -1)
            findNamedHome(nepian, "home3").isPrivate = true
        }
        homes.homeManager.playerHomes.put(nepian.uniqueId, playerHome)

        buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PUBLIC\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
            append("    home2 : world, {0, 0, 0}, PUBLIC\n")
        }.apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
        }

        playerHome.findDefaultHome(nepian).isPrivate = true

        buildString {
            append("[Homes] Home List\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
            append("    home2 : world, {0, 0, 0}, PUBLIC\n")
        }.apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
        }

        playerHome.findNamedHome(nepian, "home1").isPrivate = true
        playerHome.findNamedHome(nepian, "home2").isPrivate = true

        buildString {
            append("[Homes] No homes")
        }.apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
        }
    }
}
