package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.datas.FeeData
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_NAMED_HOME
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_DEFAULT_HOME_PRIVATE
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_DEFAULT_HOME_PUBLIC
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_NAMED_HOME_PRIVATE
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_NAMED_HOME_PUBLIC
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.SET_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.SET_NAMED_HOME
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.defaultLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.feeFile
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.minene
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.mockPluginManager
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.mockServicesManager
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.namedLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.pluginCommand
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.pluginFolder
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.registeredServiceProvider
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.spyLogger
import com.masahirosaito.spigot.homes.testutils.lastMsg
import net.milkbowl.vault.economy.Economy
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
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.doReturn
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File


@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class,
        PluginManager::class, ServicesManager::class, RegisteredServiceProvider::class)
class HomesTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun プラグインフォルダが生成されている() {
        assertTrue(pluginFolder.exists())
    }

    @Test
    fun 設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "configs.json").exists())
    }

    @Test
    fun データファイルが生成されている() {
        assertTrue(File(pluginFolder, "playerhomes.json").exists())
    }

    @Test
    fun 料金設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "fee.json").exists())
    }

    @Test
    fun ホームコマンドが登録されている() {
        assertTrue(homes.getCommand("home").executor is HomeCommand)
    }

    @Test
    fun 料金設定ファイルが読み込まれている() {
        assertThat(homes.fee, `is`(loadData(feeFile, FeeData::class.java)))
    }

    @Test
    fun エコノミーが読み込まれている() {
        assertThat(homes.econ, `is`(notNullValue()))
    }

    @Test
    fun ホームへ移動するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", null)
        assertThat(nepian.location, `is`(defaultLocation))
    }

    @Test
    fun 名前付きホームへ移動するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertThat(nepian.location, `is`(namedLocation))
    }

    @Test
    fun 他人のホームへ移動するコマンドの実行ができる() {
        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 他人の名前付きホームへ移動するコマンドの実行ができる() {
        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun ホームを設定するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        assertThat(nepian.lastMsg(), `is`(SET_DEFAULT_HOME()))
    }

    @Test
    fun 名前付きホームを設定するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        assertThat(nepian.lastMsg(), `is`(SET_NAMED_HOME("home1")))
    }

    @Test
    fun 設定されたホームを削除するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertThat(nepian.lastMsg(), `is`(DELETE_DEFAULT_HOME()))
    }

    @Test
    fun 設定された名前付きホームを削除するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertThat(nepian.lastMsg(), `is`(DELETE_NAMED_HOME("home1")))
    }

    @Test
    fun 設定されたホームをプライベート化するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        assertThat(nepian.lastMsg(), `is`(SET_DEFAULT_HOME_PRIVATE()))
    }

    @Test
    fun 設定された名前付きホームをプライベート化するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        assertThat(nepian.lastMsg(), `is`(SET_NAMED_HOME_PRIVATE("home1")))
    }

    @Test
    fun 設定されたホームをパブリック化するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off"))
        assertThat(nepian.lastMsg(), `is`(SET_DEFAULT_HOME_PUBLIC()))
    }

    @Test
    fun 設定された名前付きホームをパブリック化するコマンドの実行ができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off", "home1"))
        assertThat(nepian.lastMsg(), `is`(SET_NAMED_HOME_PUBLIC("home1")))
    }

    @Test
    fun 設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(command.onCommand(nepian, pluginCommand, "home", arrayOf("list")))
    }

    @Test
    fun 他の人の設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian")))
    }
}
