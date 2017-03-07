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
import org.hamcrest.CoreMatchers.*
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
class HomeCommandTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var pluginCommand: PluginCommand
    lateinit var command: CommandExecutor
    lateinit var nepian: Player
    lateinit var minene: Player

    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location

    lateinit var nepianLocation: Location
    lateinit var mineneLocation: Location

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", mockServer)
        minene = MockPlayerFactory.makeNewMockPlayer("Minene", mockServer)

        nepian.setOps()
        minene.setOps()

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        defaultLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Successfully set as default home"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        namedLocation = nepian.location
        command.onCommand(nepian, pluginCommand, "home", arrayOf("set", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Successfully set as home named <home1>"))

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        assertThat(nepian.location, `is`(not(namedLocation)))

        minene.teleport(MockWorldFactory.makeRandomLocation())
        assertThat(minene.location, `is`(not(namedLocation)))

        nepianLocation = nepian.location
        mineneLocation = minene.location
    }

    @After
    fun tearDown() {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }
        minene.logger.logs.forEachIndexed { i, s -> println("[Minene] $i -> $s") }

        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun 引数が間違っている場合に使い方を表示する() {
        buildString {
            append("[Homes] The argument is incorrect\n")
            append("home command usage:\n")
            append("/home : Teleport to your default home\n")
            append("/home <home_name> : Teleport to your named home\n")
            append("/home -p <player_name> : Teleport to player's default home\n")
            append("/home <home_name> -p <player_name> : Teleport to player's named home")
        }.apply {
            command.onCommand(nepian, pluginCommand, "home", arrayOf("-p"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "home2"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Minene", "home2"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "home2", "-p", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun コマンドの実行には親権限が必要() {
        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", null)
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun プレイヤーホームの実行にはプライヤーホーム権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.player>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホームの実行には名前付きホーム権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PLAYER)

        "[Homes] You don't have permission <homes.command.name>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きプレイヤーホームを実行するには名前付きプレイヤーホーム権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PLAYER, Permission.HOME_NAME)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Minene"))
        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.player.name>"))
    }

    @Test
    fun 親権限を持っている場合はホームコマンドで設定したデフォルトホームへ移動できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", null)

        assertThat(nepian.location, `is`(defaultLocation))
    }

    @Test
    fun プレイヤーホーム権限を持っている場合はプレイヤーコマンドで他のプレイヤーのデフォルトホームへ移動できる() {
        minene.setOps(false)
        minene.set(Permission.HOME, Permission.HOME_PLAYER)
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))

        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 名前付きホーム権限を持っている場合は名前付きホームコマンドで他のプレイヤーの名前付きホームへ移動できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_NAME)
        nepian.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))

        assertThat(nepian.location, `is`(namedLocation))
    }

    @Test
    fun 名前付きプレイヤーホーム権限を持っている場合は名前付きプレイヤーホームコマンドで他のプレイヤーの名前付きホームへ移動できる() {
        minene.setOps(false)
        minene.set(Permission.HOME, Permission.HOME_PLAYER, Permission.HOME_NAME, Permission.HOME_PLAYER_NAME)
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))

        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun デフォルトホームが存在しない状態でホームを実行した場合はメッセージを表示し終わる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertThat(homes.homeManager.findPlayerHome(nepian).defaultHomeData, `is`(nullValue()))

        "[Homes] Nepian's default home does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", null)
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(nepianLocation))

            command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(mineneLocation))
        }
    }

    @Test
    fun 名前付きホームが存在しない状態で名前付きホームを実行した場合はメッセージを表示し終わる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertThat(homes.homeManager.findPlayerHome(nepian).haveName("home1"), `is`(false))

        "[Homes] Nepian's home named <home1> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(nepianLocation))

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(mineneLocation))
        }
    }

    @Test
    fun プレイヤーホームを存在しないプレイヤーを指定して実行した場合はメッセージを表示し終わる() {

        "[Homes] Player <Moichi> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Moichi"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(nepianLocation))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Moichi"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(nepianLocation))
        }
    }

    @Test
    fun 名前付きホーム設定がオフの場合はメッセージを表示し終わる() {

        homes.configs.copy(onNamedHome = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }

        "[Homes] Not allowed by the configuration of this server".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(nepianLocation))

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(mineneLocation))
        }
    }

    @Test
    fun プレイヤーホーム設定がオフの場合はメッセージを表示し終わる() {

        homes.configs.copy(onFriendHome = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }

        "[Homes] Not allowed by the configuration of this server".apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(mineneLocation))

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(mineneLocation))
        }
    }

    @Test
    fun デフォルトホームがプライベートの状態で自分以外のプレイヤーは移動できない() {

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        assertThat(homes.homeManager.findDefaultHome(nepian).isPrivate, `is`(true))

        command.onCommand(nepian, pluginCommand, "home", null)
        assertThat(nepian.location, `is`(defaultLocation))

        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
        assertThat(minene.lastMsg(), `is`("[Homes] Nepian's default home is PRIVATE"))
        assertThat(minene.location, `is`(mineneLocation))
    }

    @Test
    fun 名前付きホームがプライベートの状態で自分以外のプレイヤーは移動できない() {

        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        assertThat(homes.homeManager.findNamedHome(nepian, "home1").isPrivate, `is`(true))

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertThat(nepian.location, `is`(namedLocation))

        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
        assertThat(minene.lastMsg(), `is`("[Homes] Nepian's home named <home1> is PRIVATE"))
        assertThat(minene.location, `is`(mineneLocation))
    }
}
