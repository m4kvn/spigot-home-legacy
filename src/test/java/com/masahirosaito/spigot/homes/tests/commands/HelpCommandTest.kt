package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.maincommands.HomeCommand
import com.masahirosaito.spigot.homes.commands.subcommands.helpcommands.HelpCommand
import com.masahirosaito.spigot.homes.commands.subcommands.InviteCommand
import com.masahirosaito.spigot.homes.tests.Permission
import com.masahirosaito.spigot.homes.tests.utils.*
import org.bukkit.*
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class)
class HelpCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var nepian: Player

    lateinit var homeCommand: HomeCommand
    lateinit var helpCommand: HelpCommand
    lateinit var inviteCommand: InviteCommand

    private fun msgCommandDescriptions() = buildString {
        append("Homes command list\n")
        append("/home help <command_name> : Display the usage of command\n")
        homeCommand.subCommands.forEach {
            append("  ${it.name} : ${it.description}\n")
        }
    }

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        homeCommand = HomeCommand(homes)
        helpCommand = HelpCommand(homeCommand)
        inviteCommand = InviteCommand(homes)
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)

        nepian.setOps()
    }

    @After
    fun tearDown() {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }

        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun 引数が間違っている場合は使い方を表示し終わる() {
        buildString {
            append("[Homes] The argument is incorrect\n")
            append("help command usage:\n")
            append("/home help : Display the list of Homes commands\n")
            append("/home help <command_name> : Display the usage of Homes command")
        }.apply {
            command.onCommand(nepian, pluginCommand, "home", arrayOf("help", "invite", "delete"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun コマンドの実行には親権限が必要() {
        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help", "invite"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun ヘルプコマンドの実行には権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.help>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 使い方コマンドの実行には権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.help.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help", "invite"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun ヘルプ権限を持っている場合はヘルプコマンドを実行できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_HELP)

        "[Homes] ${msgCommandDescriptions()}".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 使い方権限を持っている場合は使い方コマンドを実行できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_HELP_COMMAND)

        "[Homes] ${ChatColor.stripColor(inviteCommand.usage.toString())}".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help", "invite"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 指定したコマンドが存在しない場合にメッセージを表示し終了する() {

        "[Homes] Command <remove> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("help", "remove"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }
}
