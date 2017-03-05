package com.masahirosaito.spigot.homes.tests

import com.masahirosaito.spigot.homes.Homes
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

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class PrivateCommandTest {
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
        Assert.assertEquals("[Homes] Successfully set as default home", logs.last())

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))
        Assert.assertEquals("[Homes] Successfully set as home named <home1>", logs.last())

        minene.teleport(MockWorldFactory.makeRandomLocation())
    }

    @After
    fun tearDown() {
        Assert.assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun 親権限を持っていない場合は全ての機能を実行できない() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        Assert.assertEquals("[Homes] You don't have permission <homes.command.private>", logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off"))
        Assert.assertEquals("[Homes] You don't have permission <homes.command.private>", logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        Assert.assertEquals("[Homes] You don't have permission <homes.command.private>", logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off", "home1"))
        Assert.assertEquals("[Homes] You don't have permission <homes.command.private>", logs.last())
    }

    @Test
    fun コマンドを実行するとデフォルトホームをプライベート化できる() {
        nepian.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        Assert.assertEquals("[Homes] Set your default home PRIVATE", logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off"))
        Assert.assertEquals("[Homes] Set your default home PUBLIC", logs.last())
    }

    @Test
    fun コマンドを実行すると名前付きホームをプライベート化できる() {
        nepian.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        Assert.assertEquals("[Homes] Set your home named home1 PRIVATE", logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off", "home1"))
        Assert.assertEquals("[Homes] Set your home named home1 PUBLIC", logs.last())
    }

    @Test
    fun 引数が間違っていた場合はコマンドの使い方を表示する() {
        nepian.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private"))
        Assert.assertEquals(buildString {
            append("[Homes] The argument is incorrect\n")
            append("private command usage:\n")
            append("/home private (on/off) : Set your default home private or public\n")
            append("/home private (on/off) <home_name> : Set your named home private or public")
        }, logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "aaa"))
        Assert.assertEquals(buildString {
            append("[Homes] The argument is incorrect\n")
            append("private command usage:\n")
            append("/home private (on/off) : Set your default home private or public\n")
            append("/home private (on/off) <home_name> : Set your named home private or public")
        }, logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1", "home2"))
        Assert.assertEquals(buildString {
            append("[Homes] The argument is incorrect\n")
            append("private command usage:\n")
            append("/home private (on/off) : Set your default home private or public\n")
            append("/home private (on/off) <home_name> : Set your named home private or public")
        }, logs.last())
    }

    @Test
    fun 他の人はプライベート化したデフォルトホームに移動できない() {
        nepian.setOps()
        minene.set(Permission.HOME, Permission.HOME_PLAYER)

        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
        Assert.assertEquals(defaultLocation, minene.location)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        Assert.assertEquals("[Homes] Set your default home PRIVATE", logs.last())

        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
        Assert.assertEquals("[Homes] Nepian's default home is PRIVATE", logs.last())
    }

    @Test
    fun 他の人はプライベート化した名前付きホームに移動できない() {
        nepian.setOps()
        minene.set(Permission.HOME, Permission.HOME_PLAYER, Permission.HOME_PLAYER_NAME, Permission.HOME_NAME)

        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        Assert.assertEquals(namedLocation, minene.location)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        Assert.assertEquals("[Homes] Set your home named home1 PRIVATE", logs.last())

        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        Assert.assertEquals("[Homes] Nepian's home named home1 is PRIVATE", logs.last())
    }

    @Test
    fun プライベート化したホームは自分以外からリスト表示されない() {
        nepian.setOps()
        minene.setOps()

        command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
        Assert.assertEquals(buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PUBLIC\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
        }, logs.last())

        command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian"))
        Assert.assertEquals(buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PUBLIC\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
        }, logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        Assert.assertEquals("[Homes] Set your default home PRIVATE", logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
        Assert.assertEquals(buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PRIVATE\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
        }, logs.last())

        command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian"))
        Assert.assertEquals(buildString {
            append("[Homes] Home List\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PUBLIC\n")
        }, logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        Assert.assertEquals("[Homes] Set your home named home1 PRIVATE", logs.last())

        command.onCommand(nepian, pluginCommand, "home", arrayOf("list"))
        Assert.assertEquals(buildString {
            append("[Homes] Home List\n")
            append("  [Default] world, {0, 0, 0}, PRIVATE\n")
            append("  [Named Home]\n")
            append("    home1 : world, {0, 0, 0}, PRIVATE\n")
        }, logs.last())

        command.onCommand(minene, pluginCommand, "home", arrayOf("list", "Nepian"))
        Assert.assertEquals("[Homes] No homes", logs.last())
    }
}
