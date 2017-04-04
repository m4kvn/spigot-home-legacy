package com.masahirosaito.spigot.homes.commands.subcommands.listcommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission.home_admin
import com.masahirosaito.spigot.homes.Permission.home_command_list
import com.masahirosaito.spigot.homes.Permission.home_command_list_player
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.strings.ErrorStrings.ARGUMENT_INCORRECT
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NOT_ALLOW_BY_CONFIG
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_HOME
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_PERMISSION
import com.masahirosaito.spigot.homes.testutils.*
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homeConsoleCommandSender
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.minene
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.spyLogger
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
import org.hamcrest.CoreMatchers.not
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
class ListPlayerCommandTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun リストコマンド権限を持っていない場合はコマンドの実行ができない() {
        minene.setOps(false)
        minene.executeHomeCommand("list", "Nepian")
        assertThat(minene.lastMsg(), `is`(NO_PERMISSION(home_command_list)))
    }

    @Test
    fun リストプレイヤーコマンド権限を持っていない場合はコマンドの実行ができない() {
        minene.setOps(false)
        minene.setPermissions(home_command_list)
        minene.executeHomeCommand("list", "Nepian")
        assertThat(minene.lastMsg(), `is`(NO_PERMISSION(home_command_list_player)))
    }

    @Test
    fun プレイヤーホーム設定がオフの場合はコマンドの実行ができない() {
        Configs.onFriendHome = false
        minene.executeHomeCommand("list", "Nepian")
        assertThat(minene.lastMsg(), `is`(NOT_ALLOW_BY_CONFIG()))
    }

    @Test
    fun コマンドの引数が間違っていた場合に使い方を表示する() {
        minene.executeHomeCommand("list", "Nepian", "Minene")
        assertThat(minene.lastMsg(), `is`(ARGUMENT_INCORRECT(ListCommand(homes).usage.toString())))
    }

    @Test
    fun 管理者権限を持っていない場合は他人のプライベートホームは表示できない() {
        PlayerDataManager.apply {
            setDefaultHomePrivate(nepian, true)
            setNamedHomePrivate(nepian, "home1", true)
        }
        minene.setOps(false)
        minene.setPermissions(home_command_list, home_command_list_player)
        minene.executeHomeCommand("list", "Nepian")
        assertThat(minene.lastMsg(), `is`(NO_HOME(nepian.name)))
    }

    @Test
    fun 管理者権限を持っている場合は他人のプライベートホームも表示できる() {
        PlayerDataManager.apply {
            setDefaultHomePrivate(nepian, true)
            setNamedHomePrivate(nepian, "home1", true)
        }
        minene.setOps(false)
        minene.setPermissions(home_admin)
        minene.executeHomeCommand("list", "Nepian")
        assertThat(minene.lastMsg(), `is`(not(NO_HOME(nepian.name))))
    }

    @Test
    fun コンソールから実行した場合はプライベートホームも表示できる() {
        PlayerDataManager.apply {
            setDefaultHomePrivate(nepian, true)
            setNamedHomePrivate(nepian, "home1", true)
        }
        homeConsoleCommandSender.executeHomeCommand("list", "Nepian")
        assertThat(spyLogger.logs.lastOrNull(), `is`(not(NO_HOME(nepian.name))))
    }
}
