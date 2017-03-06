package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class HomesTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var logs: MutableList<String>

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun 設定ファイルの生成() {
        assertThat(File(TestInstanceCreator.pluginFolder, "configs.json").exists(), `is`(true))
    }

    @Test
    fun データファイルの生成() {
        assertThat(File(TestInstanceCreator.pluginFolder, "playerhomes.json").exists(), `is`(true))
    }

    @Test
    fun コマンドの登録() {
        assertThat(homes.getCommand("home").executor, `is`(notNullValue()))
    }
}
