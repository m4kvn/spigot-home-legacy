package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.commands.DeleteCommandData
import com.masahirosaito.spigot.homes.tests.commands.HomeCommandData
import com.masahirosaito.spigot.homes.tests.commands.SetCommandData
import com.masahirosaito.spigot.homes.tests.exceptions.CanNotFindDefaultHomeException
import com.masahirosaito.spigot.homes.tests.exceptions.CanNotFindNamedHomeException
import com.masahirosaito.spigot.homes.tests.exceptions.CommandArgumentIncorrectException
import com.masahirosaito.spigot.homes.tests.exceptions.NotHavePermissionException
import com.masahirosaito.spigot.homes.tests.utils.*
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
class DeleteCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player

    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        logs = TestInstanceCreator.spyLogger.logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)

        nepian.set(Permission.HOME_DEFAULT, Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER, Permission.HOME_PLAYER_NAME)
        nepian.set(Permission.HOME_SET, Permission.HOME_SET_NAME)

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        assertEquals(SetCommandData.msgSuccessSetDefaultHome(), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        assertEquals(SetCommandData.msgSuccessSetNamedHome("home1"), logs.last())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コマンドの親権限を持っていない場合() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertEquals(DeleteCommandData.msg(NotHavePermissionException(Permission.HOME_DELETE)), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(defaultLocation, nepian.location)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertEquals(DeleteCommandData.msg(NotHavePermissionException(Permission.HOME_DELETE)), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(namedLocation, nepian.location)
    }

    @Test
    fun 名前付きホーム設定の権限を持っていない場合() {
        nepian.set(Permission.HOME_DELETE)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertEquals(DeleteCommandData.getResultMessage(null), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertEquals(DeleteCommandData.msg(NotHavePermissionException(Permission.HOME_DELETE_NAME)), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(namedLocation, nepian.location)
    }

    @Test
    fun このコマンドの全ての権限を持っている場合() {
        nepian.set(Permission.HOME_DELETE, Permission.HOME_DELETE_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertEquals(DeleteCommandData.getResultMessage(null), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertEquals(DeleteCommandData.getResultMessage("home1"), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())
    }

    @Test
    fun プレイヤーが管理者の場合() {
        nepian.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertEquals(DeleteCommandData.getResultMessage(null), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertEquals(DeleteCommandData.getResultMessage("home1"), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())
    }

    @Test
    fun 引数が間違っている場合() {
        nepian.set(Permission.HOME_DELETE, Permission.HOME_DELETE_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1", "home2"))
        assertEquals(DeleteCommandData.msg(CommandArgumentIncorrectException(DeleteCommandData)), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(defaultLocation, nepian.location)

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(namedLocation, nepian.location)
    }

    @Test
    fun デフォルトホームが設定されていない場合() {
        nepian.set(Permission.HOME_DELETE, Permission.HOME_DELETE_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertEquals(DeleteCommandData.getResultMessage(null), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertEquals(DeleteCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)
        assertEquals(HomeCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())
    }

    @Test
    fun 名前付きホームが設定されていない場合() {
        nepian.set(Permission.HOME_DELETE, Permission.HOME_DELETE_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertEquals(DeleteCommandData.getResultMessage("home1"), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertEquals(DeleteCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertEquals(HomeCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())
    }
}
