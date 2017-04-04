package com.masahirosaito.spigot.homes.commands.subcommands.listcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.HOME_LIST
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.PLAYER_LIST
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homeConsoleCommandSender
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.executeHomeCommand
import com.masahirosaito.spigot.homes.testutils.lastMsg
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class,
        PluginManager::class, ServicesManager::class, RegisteredServiceProvider::class)
class ListCommandTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コンソールからコマンドを実行した場合はプレイヤーリストを表示する() {
        homeConsoleCommandSender.executeHomeCommand("list")
        assertThat(homeConsoleCommandSender.lastMsg(), `is`(PLAYER_LIST()))
    }

    @Test
    fun プレイヤーからコマンドを実行した場合はホームリストを表示する() {
        nepian.executeHomeCommand("list")
        assertThat(nepian.lastMsg(), `is`(HOME_LIST(PlayerDataManager.findPlayerData(nepian), false)))
    }
}
