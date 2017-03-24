package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.commands.maincommands.HomeCommand
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.defaultLocation
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.economy
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.feeFile
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.minene
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.pluginCommand
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
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class HomePlayerCommandTest {
    lateinit var homePlayerCommand: HomePlayerCommand

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        homePlayerCommand = HomePlayerCommand(command as HomeCommand)
    }

    @After
    fun tearDown() {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }
        minene.logger.logs.forEachIndexed { i, s -> println("[Minene] $i -> $s") }

        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun fee() {

    }

    @Test
    fun configs() {

    }

    @Test
    fun isValidArgs() {

    }

    @Test
    fun execute() {
        homePlayerCommand.execute(minene, arrayListOf("-p", "Nepian"))
        assertEquals(defaultLocation, minene.location)
    }
}
