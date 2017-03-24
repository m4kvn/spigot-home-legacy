package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.Permission
import com.masahirosaito.spigot.homes.tests.utils.*
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.mockServer
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.pluginCommand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.java.JavaPluginLoader
import org.hamcrest.CoreMatchers.*
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
class HomeCommandTest {
    lateinit var command: CommandExecutor
    lateinit var nepian: Player
    lateinit var minene: Player

    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())

        command = pluginCommand.executor
        nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", homes)
        minene = MockPlayerFactory.makeNewMockPlayer("Minene", homes)

        nepian.setOps()
        minene.setOps()

        homes.homeManager.findPlayerHome(nepian).apply {
            setDefaultHome(nepian)
            defaultLocation = findDefaultHome(nepian).location()
            setNamedHome(nepian, "home1", -1)
            namedLocation = findNamedHome(nepian, "home1").location()
        }

        assertThat(defaultLocation, `is`(nepian.location))
        assertThat(namedLocation, `is`(nepian.location))

        while (defaultLocation == nepian.location || namedLocation == nepian.location) {
            nepian.teleport(MockWorldFactory.makeRandomLocation())
        }

        while (defaultLocation == minene.location || namedLocation == minene.location) {
            minene.teleport(MockWorldFactory.makeRandomLocation())
        }
    }

    @After
    fun tearDown() {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }
        minene.logger.logs.forEachIndexed { i, s -> println("[Minene] $i -> $s") }

        assertTrue(TestInstanceCreator.tearDown())
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
        }
    }

    @Test
    fun 名前付きホームの実行には名前付きホーム権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.name>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きプレイヤーホームを実行するには名前付きプレイヤーホーム権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.player.name>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 親権限を持っている場合はホームコマンドで設定したデフォルトホームへ移動できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", null)

        assertThat(nepian.location, `is`(defaultLocation))
    }

    @Test
    fun プレイヤーホーム権限を持っている場合はプレイヤーコマンドで他のプレイヤーのデフォルトホームへ移動できる() {
        minene.setOps(false)
        minene.set(Permission.HOME, Permission.HOME_PLAYER)
        command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))

        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 名前付きホーム権限を持っている場合は名前付きホームコマンドで他のプレイヤーの名前付きホームへ移動できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))

        assertThat(nepian.location, `is`(namedLocation))
    }

    @Test
    fun 名前付きプレイヤーホーム権限を持っている場合は名前付きプレイヤーホームコマンドで他のプレイヤーの名前付きホームへ移動できる() {
        minene.setOps(false)
        minene.set(Permission.HOME, Permission.HOME_PLAYER_NAME)
        command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))

        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun デフォルトホームが存在しない状態でホームを実行した場合はメッセージを表示し終わる() {
        homes.homeManager.findPlayerHome(nepian).removeDefaultHome(nepian)
        assertNull(homes.homeManager.findPlayerHome(nepian).defaultHomeData)

        "[Homes] Nepian's default home does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", null)
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(not(defaultLocation)))

            command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(not(defaultLocation)))
        }
    }

    @Test
    fun 名前付きホームが存在しない状態で名前付きホームを実行した場合はメッセージを表示し終わる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertThat(homes.homeManager.findPlayerHome(nepian).haveName("home1"), `is`(false))

        "[Homes] Nepian's home named <home1> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(not(namedLocation)))

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(not(namedLocation)))
        }
    }

    @Test
    fun プレイヤーホームを存在しないプレイヤーを指定して実行した場合はメッセージを表示し終わる() {

        "[Homes] Player <Moichi> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("-p", "Moichi"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(not(defaultLocation)))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("home1", "-p", "Moichi"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(nepian.location, `is`(not(namedLocation)))
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
            assertThat(nepian.location, `is`(not(namedLocation)))

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(not(namedLocation)))
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
            assertThat(minene.location, `is`(not(defaultLocation)))

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(not(namedLocation)))
        }
    }

    @Test
    fun デフォルトホームがプライベートの状態で自分以外のプレイヤーは移動できない() {
        homes.homeManager.findDefaultHome(nepian).isPrivate = true
        assertTrue(homes.homeManager.findDefaultHome(nepian).isPrivate)

        command.onCommand(nepian, pluginCommand, "home", null)
        assertThat(nepian.location, `is`(defaultLocation))

        "[Homes] Nepian's default home is PRIVATE".apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(not(defaultLocation)))
        }
    }

    @Test
    fun 名前付きホームがプライベートの状態で自分以外のプレイヤーは移動できない() {
        homes.homeManager.findNamedHome(nepian, "home1").isPrivate = true
        assertTrue(homes.homeManager.findNamedHome(nepian, "home1").isPrivate)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("home1"))
        assertThat(nepian.location, `is`(namedLocation))

        "[Homes] Nepian's home named <home1> is PRIVATE".apply {

            command.onCommand(minene, pluginCommand, "home", arrayOf("home1", "-p", "Nepian"))
            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.location, `is`(not(namedLocation)))
        }
    }
}
