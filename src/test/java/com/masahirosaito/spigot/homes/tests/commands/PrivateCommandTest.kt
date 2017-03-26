package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.Permission
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.pluginCommand
import com.masahirosaito.spigot.homes.tests.utils.lastMsg
import com.masahirosaito.spigot.homes.tests.utils.set
import com.masahirosaito.spigot.homes.tests.utils.setOps
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
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
class PrivateCommandTest {

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun 引数が間違っている場合に使い方を表示する() {
        buildString {
            append("[Homes] The argument is incorrect\n")
            append("private command usage:\n")
            append("/home private (on/off) : Set your default home private or public\n")
            append("/home private (on/off) <home_name> : Set your named home private or public")
        }.apply {
            command.onCommand(nepian, pluginCommand, "home", arrayOf("private"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "aaa"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1", "home2"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun コマンドの実行には親権限が必要() {
        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun デフォルトホームのプライベート化にはプライベート権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.private>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホームのプライベート化には名前付きプライベート権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PRIVATE)

        "[Homes] You don't have permission <homes.command.private.name>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun プライベート権限を持っている場合はデフォルトホームをプライベート化できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PRIVATE)

        "[Homes] Set your default home PRIVATE".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きプライベート権限を持っている場合は名前付きホームをプライベート化できる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_PRIVATE, Permission.HOME_PRIVATE_NAME)

        "[Homes] Set your home named <home1> PRIVATE".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun プライベート化機能が設定でオフの場合はデフォルトホームをプライベート化できない() {
        homes.configs = homes.configs.copy(onPrivate = false)
        assertThat(homes.configs.onPrivate, `is`(false))

        "[Homes] Not allowed by the configuration of this server".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun プライベート化機能が設定でオフの場合は名前付きホームをプライベート化できない() {
        homes.configs = homes.configs.copy(onPrivate = false)
        assertThat(homes.configs.onPrivate, `is`(false))

        "[Homes] Not allowed by the configuration of this server".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホーム機能が設定でオフの場合は名前付きホームをプライベート化できない() {
        homes.configs = homes.configs.copy(onNamedHome = false)
        assertThat(homes.configs.onNamedHome, `is`(false))

        "[Homes] Not allowed by the configuration of this server".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun デフォルトホームが存在しない状態でプライベートを行った場合はメッセージを送信し終わる() {
        homes.homeManager.findPlayerHome(nepian).removeDefaultHome(nepian)

        "[Homes] Nepian's default home does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホームが存在しない状態でプライベートを行った場合はメッセージを送信し終わる() {
        homes.homeManager.findPlayerHome(nepian).removeNamedHome(nepian, "home1")

        "[Homes] Nepian's home named <home1> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun プライベート化したデフォルトホームをパブリック化できる() {
        homes.homeManager.findDefaultHome(nepian).isPrivate = true

        "[Homes] Set your default home PUBLIC".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun プライベート化した名前付きホームをパブリック化できる() {
        homes.homeManager.findNamedHome(nepian, "home1").isPrivate = true
        
        "[Homes] Set your home named <home1> PUBLIC".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "off", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }
}
