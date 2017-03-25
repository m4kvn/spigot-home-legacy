package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.subcommands.setcommands.SetCommand
import com.masahirosaito.spigot.homes.commands.subcommands.setcommands.SetNameCommand
import com.masahirosaito.spigot.homes.tests.utils.MyVault
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.tests.utils.lastMsg
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
        PluginManager::class, ServicesManager::class, MyVault::class, RegisteredServiceProvider::class)
class SetCommandTest {
    lateinit var setCommand: SetCommand

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        setCommand = SetCommand(homes)
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コマンドの名前が適切である() {
        assertThat(setCommand.name, `is`("set"))
    }

    @Test
    fun コマンドの説明文が記述されている() {
        assertTrue(setCommand.description.isNotBlank())
    }

    @Test
    fun 権限が適切に返される() {
        setCommand.permissions.containsAll(listOf(
                "homes.command",
                "homes.command.set"
        ))
    }

    @Test
    fun コマンドの使い方が記述されている() {
        assertTrue(setCommand.usage.usage.isNotEmpty())
    }

    @Test
    fun 設定した料金を返す() {
        assertThat(setCommand.fee(), `is`(0.0))
        homes.fee = homes.fee.copy(SET = 2.0)
        assertThat(setCommand.fee(), `is`(2.0))
    }

    @Test
    fun 設定が空である() {
        assertTrue(setCommand.configs().isEmpty())
    }

    @Test
    fun 引数の確認が適正に行われる() {
        assertTrue(setCommand.isValidArgs(listOf()))
        assertFalse(setCommand.isValidArgs(listOf("home1")))
    }

    @Test
    fun サブコマンドに名前付きホームを設定するコマンドを持っている() {
        assertTrue(setCommand.commands.any { it is SetNameCommand })
    }

    @Test
    fun デフォルトホームの設定ができる() {
        homes.homeManager.findPlayerHome(nepian).removeDefaultHome(nepian)

        "[Homes] Successfully set as default home".apply {
            setCommand.execute(nepian, listOf())
            assertThat(homes.homeManager.findDefaultHome(nepian).location(), `is`(nepian.location))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun デフォルトホームを更新できる() {

        "[Homes] Successfully set as default home".apply {
            setCommand.execute(nepian, listOf())
            assertThat(homes.homeManager.findDefaultHome(nepian).location(), `is`(nepian.location))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }
}
