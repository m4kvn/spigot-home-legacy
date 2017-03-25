package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.Permission
import com.masahirosaito.spigot.homes.tests.utils.*
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.pluginCommand
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
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class,
        PluginManager::class, ServicesManager::class, MyVault::class, RegisteredServiceProvider::class)
class SetCommandTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())

        homes.homeManager.findPlayerHome(nepian).removeDefaultHome(nepian)
        homes.homeManager.findPlayerHome(nepian).removeNamedHome(nepian, "home1")
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun デフォルトホームの設定には親権限が必要() {
        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホームの設定には親権限が必要() {
        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun デフォルトホームの設定には設定権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.set>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホームの設定には名前付き設定権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.set.name>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 設定権限を持っている場合はデフォルトホームを設定できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_SET)

        "[Homes] Successfully set as default home".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付き設定権限を持っている場合は名前付きホームを設定できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_SET_NAME)

        "[Homes] Successfully set as home named <home1>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 引数が間違っていた場合に使い方を表示する() {
        buildString {
            append("[Homes] The argument is incorrect\n")
            append("set command usage:\n")
            append("/home set : Set your location to your default home\n")
            append("/home set <home_name> : Set your location to your named home")
        }.apply {
            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1", "home2"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホーム機能が設定でオフの場合は名前付きホームを設定できない() {
        homes.configs = homes.configs.copy(onNamedHome = false)
        assertThat(homes.configs.onNamedHome, `is`(false))

        "[Homes] Not allowed by the configuration of this server".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホームの制限設定が無制限の場合は名前付きホームをいくらでも設定できる() {
        homes.configs = homes.configs.copy(homeLimit = -1)
        assertThat(homes.configs.homeLimit, `is`(-1))

        repeat(1000, { i ->
            "[Homes] Successfully set as home named <home$i>".apply {

                nepian.teleport(MockWorldFactory.makeRandomLocation())
                command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home$i"))
                assertThat(nepian.lastMsg(), `is`(this))
            }
        })
    }

    @Test
    fun 名前付きホームの制限設定が設定されている場合は名前付きホームの設定できる数が制限される() {
        homes.configs = homes.configs.copy(homeLimit = 5)
        assertThat(homes.configs.homeLimit, `is`(5))

        "[Homes] You can not set more homes (Limit: 5)".apply {

            repeat(6, { i ->
                nepian.teleport(MockWorldFactory.makeRandomLocation())
                command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home$i"))
            })
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Ignore
    @Test
    fun 設定したデフォルトホームへのホームコマンドで移動できる() {
    }

//    @Test
//    fun 設定したデフォルトホームへホームコマンドで移動できる() {
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        defaultLocation = nepian.location
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
//
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(nepian, pluginCommand, "home", null)
//
//        assertThat(nepian.location, `is`(defaultLocation))
//    }
//
//    @Test
//    fun 設定した名前付きホームへ名前付きホームコマンドで移動できる() {
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        namedLocation = nepian.location
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
//
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
//
//        assertThat(nepian.location, `is`(namedLocation))
//    }
//
//    @Test
//    fun デフォルトホームが既に設定されている場合は上書きされる() {
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        defaultLocation = nepian.location
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
//
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
//
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(nepian, pluginCommand, "home", null)
//
//        assertThat(nepian.location, `is`(not(defaultLocation)))
//    }
//
//    @Test
//    fun 名前付きホームが既に設定されている場合は上書きされる() {
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        namedLocation = nepian.location
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
//
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
//
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
//
//        assertThat(nepian.location, `is`(not(namedLocation)))
//    }
//
//    @Test
//    fun 設定されたデフォルトホームに他の人が移動できる() {
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        defaultLocation = nepian.location
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
//
//        minene.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
//
//        assertThat(minene.location, `is`(defaultLocation))
//    }
//
//    @Test
//    fun 設定された名前付きホームに他の人が移動できる() {
//        nepian.teleport(MockWorldFactory.makeRandomLocation())
//        namedLocation = nepian.location
//        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
//
//        minene.teleport(MockWorldFactory.makeRandomLocation())
//        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
//
//        assertThat(minene.location, `is`(namedLocation))
//    }
}
