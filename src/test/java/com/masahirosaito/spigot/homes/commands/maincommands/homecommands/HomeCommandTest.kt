package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.strings.EconomyStrings.NOT_ENOUGH_MONEY_ERROR
import com.masahirosaito.spigot.homes.testutils.*
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.economy
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
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
class HomeCommandTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun テレポートがキャンセルされた場合はお金を消費しない() {
        homes.fee.HOME = 1000.0
        val money: Double = economy.getBalance(nepian)
        nepian.executeHomeCommand()
        nepian.cancelTeleport()
        nepian.getDelayThread()?.join()
        assertThat(economy.getBalance(nepian), `is`(money))
    }

    @Test
    fun テレポートが成功した場合はお金を消費する() {
        homes.fee.HOME = 1000.0
        val money: Double = economy.getBalance(nepian)
        nepian.executeHomeCommand()
        nepian.getDelayThread()?.join()
        assertThat(economy.getBalance(nepian), `is`(money - 1000.0))
    }

    @Test
    fun お金が足りなかった場合はテレポートに失敗する() {
        homes.fee.HOME = 1000.0
        homes.econ?.withdrawPlayer(nepian, 4500.0)
        assertTrue(economy.getBalance(nepian) < 1000.0)
        nepian.executeHomeCommand()
        nepian.getDelayThread()?.join()
        assertThat(nepian.lastMsg(), `is`(NOT_ENOUGH_MONEY_ERROR(homes.fee.HOME)))
    }
}
