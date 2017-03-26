package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.Permission
import com.masahirosaito.spigot.homes.tests.utils.*
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.tests.utils.TestInstanceCreator.pluginCommand
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
class DeleteCommandTest {

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))
    }

    @After
    fun tearDown() {
        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun 引数が間違っている場合は使い方を表示し終わる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1", "home2"))

        assertThat(nepian.lastMsg(), `is`(buildString {
            append("[Homes] The argument is incorrect\n")
            append("delete command usage:\n")
            append("/home delete : Delete your default home\n")
            append("/home delete <home_name> : Delete your named home")
        }))

        assertThat(homes.homeManager.findPlayerHome(nepian).defaultHomeData, `is`(notNullValue()))
        assertThat(homes.homeManager.findPlayerHome(nepian).haveName("home1"), `is`(true))
    }

    @Test
    fun コマンドの実行には親権限が必要() {
        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
            assertThat(nepian.lastMsg(), `is`(this))
            command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }

        assertThat(homes.homeManager.findPlayerHome(nepian).defaultHomeData, `is`(notNullValue()))
        assertThat(homes.homeManager.findPlayerHome(nepian).haveName("home1"), `is`(true))
    }

    @Test
    fun コマンドの実行には削除権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.delete>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
            assertThat(nepian.lastMsg(), `is`(this))
        }

        assertThat(homes.homeManager.findPlayerHome(nepian).defaultHomeData, `is`(notNullValue()))
        assertThat(homes.homeManager.findPlayerHome(nepian).haveName("home1"), `is`(true))
    }

    @Test
    fun 名前付きホーム削除の実行には名前付き削除権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_DELETE)

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.delete.name>"))

        assertThat(homes.homeManager.findPlayerHome(nepian).defaultHomeData, `is`(notNullValue()))
        assertThat(homes.homeManager.findPlayerHome(nepian).haveName("home1"), `is`(true))
    }

    @Test
    fun 削除権限を持っている場合はホームの削除を行える() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_DELETE)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))

        assertThat(homes.homeManager.findPlayerHome(nepian).defaultHomeData, `is`(nullValue()))
    }

    @Test
    fun 名前付き削除権限を持っている場合は名前付きホームの削除を行える() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_DELETE, Permission.HOME_DELETE_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))

        assertThat(homes.homeManager.findPlayerHome(nepian).haveName("home1"), `is`(false))
    }

    @Test
    fun ホームが存在しない場合はメッセージを表示し終了する() {
        homes.homeManager.findPlayerHome(nepian).defaultHomeData = null

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        assertThat(nepian.lastMsg(), `is`("[Homes] Nepian's default home does not exist"))
    }

    @Test
    fun 名前付きホームが存在しない場合はメッセージを表示し終了する() {
        homes.homeManager.findPlayerHome(nepian).namedHomeData.removeAll { it.name == "home1" }

        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        assertThat(nepian.lastMsg(), `is`("[Homes] Nepian's home named <home1> does not exist"))
    }
}
