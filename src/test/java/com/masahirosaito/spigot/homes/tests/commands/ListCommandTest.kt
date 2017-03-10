package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.PlayerHome
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

    @Before
    fun setUp() {
        Assert.assertThat(TestInstanceCreator.setUp(), CoreMatchers.`is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)

        nepian.setOps()
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

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help", "invite"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }
}
