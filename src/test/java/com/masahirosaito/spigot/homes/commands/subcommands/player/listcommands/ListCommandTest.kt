package com.masahirosaito.spigot.homes.commands.subcommands.player.listcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.strings.EconomyStrings.NOT_ENOUGH_MONEY_ERROR
import com.masahirosaito.spigot.homes.strings.EconomyStrings.PAY
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.HOME_LIST
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
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
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
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
    fun コマンドを実行した場合にホームリストを表示する() {
        nepian.executeHomeCommand("list")
        assertThat(nepian.lastMsg(), `is`(HOME_LIST(PlayerDataManager.findPlayerData(nepian), false)))
    }

    @Test
    fun 料金が設定されたコマンドの実行時に所持金が足りなかった場合はエラーを表示する() {
        homes.fee.LIST = 10000.0
        nepian.executeHomeCommand("list")
        assertThat(nepian.lastMsg(), `is`(NOT_ENOUGH_MONEY_ERROR(homes.fee.LIST)))
    }

    @Test
    fun 料金が設定されたコマンドの実行時に所持金が足りている場合は所持金を消費しコマンドを実行する() {
        homes.fee.LIST = 1000.0
        nepian.executeHomeCommand("list")
        assertThat(nepian.lastMsg(), `is`(PAY(1000.0.toString(), 4000.0.toString())))
    }
}
