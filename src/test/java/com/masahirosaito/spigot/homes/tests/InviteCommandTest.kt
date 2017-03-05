package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.commands.DeleteCommandData
import com.masahirosaito.spigot.homes.tests.commands.InviteCommandData
import com.masahirosaito.spigot.homes.tests.commands.SetCommandData
import com.masahirosaito.spigot.homes.tests.exceptions.CanNotFindDefaultHomeException
import com.masahirosaito.spigot.homes.tests.exceptions.CanNotFindNamedHomeException
import com.masahirosaito.spigot.homes.tests.exceptions.NotAllowedByConfigException
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
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.lang.Thread.sleep

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class InviteCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var logs: MutableList<String>
    lateinit var nepian: Player
    lateinit var minene: Player

    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location

    @Before
    fun setUp() {
        Assert.assertTrue(TestInstanceCreator.setUp())
        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        logs = TestInstanceCreator.spyLogger.logs
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        minene = MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)

        nepian.set(Permission.HOME, Permission.HOME_NAME)
        nepian.set(Permission.HOME_PLAYER, Permission.HOME_PLAYER_NAME)
        nepian.set(Permission.HOME_SET, Permission.HOME_SET_NAME)

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))
        Assert.assertEquals(SetCommandData.msgSuccessSetDefaultHome(), logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        Assert.assertEquals(SetCommandData.msgSuccessSetNamedHome("home1"), logs.last())

        minene.teleport(MockWorldFactory.makeRandomLocation())
    }

    @After
    fun tearDown() {
        Assert.assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コマンドの親権限を持っていない場合は招待許可以外は行えない() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite"))
        Assert.assertEquals(InviteCommandData.msgNoReceivedInvitation(), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        Assert.assertEquals(InviteCommandData.msg(NotHavePermissionException(Permission.HOME_INVITE)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        Assert.assertEquals(InviteCommandData.msg(NotHavePermissionException(Permission.HOME_INVITE)), logs.last())

        nepian.set(Permission.HOME_INVITE_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        Assert.assertEquals(InviteCommandData.msg(NotHavePermissionException(Permission.HOME_INVITE)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        Assert.assertEquals(InviteCommandData.msg(NotHavePermissionException(Permission.HOME_INVITE)), logs.last())
    }

    @Test
    fun 権限がなくても招待を受けることができる() {
        nepian.set(Permission.HOME_INVITE, Permission.HOME_INVITE_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        Assert.assertEquals(InviteCommandData.msgReceivedInvitationFrom(nepian), logs[logs.lastIndex - 1])
        Assert.assertEquals(InviteCommandData.msgInvited(minene), logs.last())

        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))
        Assert.assertEquals(InviteCommandData.msgAcceptedInvitationFrom(nepian), logs[logs.lastIndex-1])
        Assert.assertEquals(InviteCommandData.msgAcceptedInvitation(minene), logs.last())
        Assert.assertEquals(defaultLocation, minene.location)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        Assert.assertEquals(InviteCommandData.msgReceivedInvitationFrom(nepian, "home1"), logs[logs.lastIndex - 1])
        Assert.assertEquals(InviteCommandData.msgInvited(minene, "home1"), logs.last())

        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))
        Assert.assertEquals(InviteCommandData.msgAcceptedInvitationFrom(nepian), logs[logs.lastIndex-1])
        Assert.assertEquals(InviteCommandData.msgAcceptedInvitation(minene), logs.last())
        Assert.assertEquals(namedLocation, minene.location)
    }

    @Test
    fun 設定がオフの場合は招待機能を利用できない() {
        homes.configs.copy(onInvite = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            Assert.assertEquals(this, homes.configs)
        }
        nepian.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        Assert.assertEquals(InviteCommandData.msg(NotAllowedByConfigException()), logs.last())

        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))
        Assert.assertEquals(InviteCommandData.msg(NotAllowedByConfigException()), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        Assert.assertEquals(InviteCommandData.msg(NotAllowedByConfigException()), logs.last())

        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))
        Assert.assertEquals(InviteCommandData.msg(NotAllowedByConfigException()), logs.last())
    }

    @Test
    fun ホームが設定されていない場合は招待できない() {
        nepian.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        Assert.assertEquals(DeleteCommandData.getResultMessage(null), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        Assert.assertEquals(InviteCommandData.msg(CanNotFindDefaultHomeException(nepian)), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        Assert.assertEquals(DeleteCommandData.getResultMessage("home1"), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        Assert.assertEquals(InviteCommandData.msg(CanNotFindNamedHomeException(nepian, "home1")), logs.last())
    }

    @Test
    fun 三十秒間招待を受けなかった場合は自動的に拒否される() {
        nepian.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        Assert.assertEquals(InviteCommandData.msgReceivedInvitationFrom(nepian), logs[logs.lastIndex - 1])
        Assert.assertEquals(InviteCommandData.msgInvited(minene), logs.last())

        sleep(31000)

        Assert.assertEquals(InviteCommandData.msgCanceledInvitationFrom(nepian), logs[logs.lastIndex - 1])
        Assert.assertEquals(InviteCommandData.msgCanceledInvitation(minene), logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        Assert.assertEquals(InviteCommandData.msgReceivedInvitationFrom(nepian, "home1"), logs[logs.lastIndex - 1])
        Assert.assertEquals(InviteCommandData.msgInvited(minene, "home1"), logs.last())

        sleep(31000)

        Assert.assertEquals(InviteCommandData.msgCanceledInvitationFrom(nepian), logs[logs.lastIndex - 1])
        Assert.assertEquals(InviteCommandData.msgCanceledInvitation(minene), logs.last())
    }
}
