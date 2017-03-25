package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.defaultLocation
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.economy
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.feeFile
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.minene
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.namedLocation
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.pluginCommand
import com.masahirosaito.spigot.homes.tests.utils.lastMsg
import com.masahirosaito.spigot.homes.tests.utils.logger
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
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
class FeeDataTest {

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))
        assertThat(feeFile.exists(), `is`(true))

        economy.depositPlayer(nepian, 1000.0)
        assertThat(economy.getBalance(nepian), `is`(1000.0))

        economy.depositPlayer(minene, 1000.0)
        assertThat(economy.getBalance(minene), `is`(1000.0))
    }

    @After
    fun tearDown() {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }
        minene.logger.logs.forEachIndexed { i, s -> println("[Minene] $i -> $s") }

        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun 料金を変更したファイルが読み込まれている() {
        val copy = homes.fee.copy(
                HOME = 2.0,
                HOME_NAME = 2.0,
                HOME_PLAYER = 2.0,
                HOME_NAME_PLAYER = 2.0,
                SET = 2.0,
                SET_NAME = 2.0,
                DELETE = 2.0,
                DELETE_NAME = 2.0,
                LIST = 2.0,
                LIST_PLAYER = 2.0,
                HELP = 2.0,
                HELP_USAGE = 2.0,
                PRIVATE = 2.0,
                PRIVATE_NAME = 2.0,
                INVITE = 2.0,
                INVITE_PLAYER = 2.0,
                INVITE_PLAYER_NAME = 2.0
        )
        copy.save(feeFile)
        homes.onDisable()
        homes.onEnable()
        assertThat(homes.fee, `is`(copy))
    }

    @Test
    fun ホームコマンド実行時に料金が支払われる() {
        homes.fee = homes.fee.copy(HOME = 2.0, HOME_NAME = 2.0, HOME_PLAYER = 2.0, HOME_NAME_PLAYER = 2.0)
        assertThat(homes.fee.HOME, `is`(2.0))
        assertThat(homes.fee.HOME_NAME, `is`(2.0))
        assertThat(homes.fee.HOME_PLAYER, `is`(2.0))
        assertThat(homes.fee.HOME_NAME_PLAYER, `is`(2.0))

        "[Homes] You paid 2.0 and now have 998.0".apply {

            command.onCommand(nepian, pluginCommand, "home", null)
            assertThat(nepian.location, `is`(defaultLocation))
            assertThat(economy.getBalance(nepian), `is`(998.0))
            assertThat(nepian.lastMsg(), `is`(this))
        }

        "[Homes] You paid 2.0 and now have 996.0".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
            assertThat(nepian.location, `is`(namedLocation))
            assertThat(economy.getBalance(nepian), `is`(996.0))
            assertThat(nepian.lastMsg(), `is`(this))
        }

        "[Homes] You paid 2.0 and now have 998.0".apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
            assertThat(minene.location, `is`(defaultLocation))
            assertThat(economy.getBalance(minene), `is`(998.0))
            assertThat(minene.lastMsg(), `is`(this))
        }

        "[Homes] You paid 2.0 and now have 996.0".apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.location, `is`(namedLocation))
            assertThat(economy.getBalance(minene), `is`(996.0))
            assertThat(minene.lastMsg(), `is`(this))
        }
    }

    @Test
    fun セットコマンド実行時に料金が支払われる() {
        homes.fee = homes.fee.copy(SET = 2.0, SET_NAME = 2.0)
        assertThat(homes.fee.SET, `is`(2.0))
        assertThat(homes.fee.SET_NAME, `is`(2.0))

        "[Homes] You paid 2.0 and now have 998.0".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
            assertThat(homes.homeManager.findDefaultHome(nepian).location(), `is`(nepian.location))
            assertThat(economy.getBalance(nepian), `is`(998.0))
            assertThat(nepian.lastMsg(), `is`(this))
        }

        "[Homes] You paid 2.0 and now have 996.0".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
            assertThat(homes.homeManager.findNamedHome(nepian, "home1").location(), `is`(nepian.location))
            assertThat(economy.getBalance(nepian), `is`(996.0))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }
}
