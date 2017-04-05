package com.masahirosaito.spigot.homes.commands.subcommands.player.helpcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission.home_command_help
import com.masahirosaito.spigot.homes.Permission.home_command_help_command
import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.strings.ErrorStrings.ARGUMENT_INCORRECT
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_PERMISSION
import com.masahirosaito.spigot.homes.testutils.*
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
class PlayerHelpUsageCommandTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun ヘルプコマンド権限を持っていない場合はコマンドを実行できない() {
        nepian.setOps(false)
        nepian.executeHomeCommand("help", "help")
        assertThat(nepian.lastMsg(), `is`(NO_PERMISSION(home_command_help)))
    }

    @Test
    fun ヘルプコマンドコマンド権限を持っていない場合はコマンドを実行できない() {
        nepian.setOps(false)
        nepian.setPermissions(home_command_help)
        nepian.executeHomeCommand("help", "help")
        assertThat(nepian.lastMsg(), `is`(NO_PERMISSION(home_command_help_command)))
    }

    @Test
    fun プレイヤーからコマンドを実行した場合はプレイヤーコマンドの使い方を表示する() {
        nepian.executeHomeCommand("help", "home")
        assertThat(nepian.lastMsg(),
                `is`(HomeCommand().usage.toString()))
    }

    @Test
    fun プレイヤーから実行されたコマンドの引数が間違っている場合は使い方を表示する() {
        nepian.executeHomeCommand("help", "home", "help")
        assertThat(nepian.lastMsg(),
                `is`(ARGUMENT_INCORRECT(PlayerHelpCommand().usage.toString())))
    }
}
