package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.commands.HomeCommandData
import com.masahirosaito.spigot.homes.tests.exceptions.*
import com.masahirosaito.spigot.homes.tests.utils.MockPlayerFactory
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.configFile
import com.masahirosaito.spigot.homes.tests.utils.set
import com.masahirosaito.spigot.homes.tests.utils.setOps
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class HomeCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player
    lateinit var command: CommandExecutor

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        logs = TestInstanceCreator.spyLogger.logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コマンドの親権限を持っていない場合() {

        // デフォルトホームへの移動
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME)), logs.last())

        // 名前付きホームへの移動
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME)), logs.last())

        // プレイヤーホームへの移動
        command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME)), logs.last())

        // プレイヤーの名前付きホームへの移動
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME)), logs.last())
    }

    @Test
    fun 名前付きホームコマンドの権限を持っていない場合() {
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME_NAME)), logs.last())
    }

    @Test
    fun プレイヤーホームコマンドの権限を持っていない場合() {
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME_PLAYER)), logs.last())
    }

    @Test
    fun 名前付きホームコマンドとプレイヤーホームコマンド両方の権限を持っていない場合() {
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME_PLAYER)), logs.last())
    }

    @Test
    fun プレイヤー名前付きコマンドの権限を持っていない場合() {
        nepian.set(Permission.HOME)
        nepian.set(Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotHavePermissionException(Permission.HOME_PLAYER_NAME)), logs.last())
    }

    @Test
    fun コマンドの引数が不適切な場合() {
        nepian.set(Permission.HOME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("-p"))
        assertEquals(HomeCommandData.msg(CommandArgumentIncorrectException(HomeCommandData)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p"))
        assertEquals(HomeCommandData.msg(CommandArgumentIncorrectException(HomeCommandData)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "home2"))
        assertEquals(HomeCommandData.msg(CommandArgumentIncorrectException(HomeCommandData)), logs.last())
    }

    @Test
    fun 自分のデフォルトホームが設定されていない場合() {
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())

        nepian.set(Permission.HOME_PLAYER)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Nepian"))
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())
    }

    @Test
    fun 自分の名前付きホームが設定されていない場合() {
        nepian.set(Permission.HOME)
        nepian.set(Permission.HOME_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())

        nepian.set(Permission.HOME_PLAYER)
        nepian.set(Permission.HOME_PLAYER_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertEquals(HomeCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())
    }

    @Test
    fun 他のプレイヤーのデフォルトホームが設定されていない場合() {
        val minene = MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)
        nepian.set(Permission.HOME)
        nepian.set(Permission.HOME_PLAYER)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Minene"))
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(minene)), logs.last())
    }

    @Test
    fun 他のプレイヤーの名前付きホームが設定されていない場合() {
        val minene = MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)
        nepian.set(Permission.HOME)
        nepian.set(Permission.HOME_PLAYER)
        nepian.set(Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Minene"))
        assertEquals(HomeCommandData.msg(CanNotFindNamedHomeException(minene, "home1")), logs.last())
    }

    @Test
    fun 存在しないプレイヤーを指定した場合() {
        nepian.set(Permission.HOME)
        nepian.set(Permission.HOME_PLAYER)
        nepian.set(Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Moichi"))
        assertEquals(HomeCommandData.msg(CanNotFindOfflinePlayerException("Moichi")), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Moichi"))
        assertEquals(HomeCommandData.msg(CanNotFindOfflinePlayerException("Moichi")), logs.last())
    }

    @Test
    fun 名前付きホーム設定がオフの場合() {
        nepian.setOps()
        homes.configs.copy(onNamedHome = false).apply {
            save(configFile)
            homes.onDisable()
            homes.onEnable()
            assertEquals(this, homes.configs)
        }

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(NotAllowedByConfigException()), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotAllowedByConfigException()), logs.last())
    }

    @Test
    fun プレイヤーホーム設定がオフの場合() {
        nepian.setOps()
        homes.configs.copy(onFriendHome = false).apply {
            save(configFile)
            homes.onDisable()
            homes.onEnable()
            assertEquals(this, homes.configs)
        }

        command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotAllowedByConfigException()), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertEquals(HomeCommandData.msg(NotAllowedByConfigException()), logs.last())
    }
}
