package com.masahirosaito.spigot.homes.commands.subcommands.console.helpcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.strings.ErrorStrings.ARGUMENT_INCORRECT
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_COMMAND
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homeConsoleCommandSender
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
class ConsoleHelpUsageCommandTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コンソールからコマンドを実行した場合はコンソールコマンド以外の使い方は表示できない() {
        homeConsoleCommandSender.executeHomeCommand("help", "home")
        assertThat(homeConsoleCommandSender.lastMsg(), `is`(NO_COMMAND("home")))
    }

    @Test
    fun コンソールからコマンドを実行した場合はコンソールコマンドの使い方を表示する() {
        homeConsoleCommandSender.executeHomeCommand("help", "help")
        assertThat(homeConsoleCommandSender.lastMsg(),
                `is`(ConsoleHelpCommand().usage.toString()))
    }

    @Test
    fun コンソールから実行されたコマンドの引数が間違っている場合は使い方を表示する() {
        homeConsoleCommandSender.executeHomeCommand("help", "home", "help")
        assertThat(homeConsoleCommandSender.lastMsg(),
                `is`(ARGUMENT_INCORRECT(ConsoleHelpCommand().usage.toString())))
    }
}
