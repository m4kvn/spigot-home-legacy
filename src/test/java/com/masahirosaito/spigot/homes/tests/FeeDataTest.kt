package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.feeFile
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
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var logs: MutableList<String>

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))
        assertThat(feeFile.exists(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
    }

    @After
    fun tearDown() {
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

    }
}
