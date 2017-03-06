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
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
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

    private fun Player.acceptInvitation() {
        if (this.hasMetadata("homes.invite")) {
            val th = (this.getMetadata("homes.invite")[0].value() as Thread)
            if (th.isAlive) {
                th.interrupt()
                th.join()
            }
            assertThat(th.isAlive, `is`(false))
        }
        assertThat(this.hasMetadata("homes.invite"), `is`(false))
    }

    @Before
    fun setUp() {
        assertThat(TestInstanceCreator.setUp(), `is`(true))

        mockServer = TestInstanceCreator.mockServer
        homes = TestInstanceCreator.homes
        pluginCommand = homes.getCommand("home")
        command = pluginCommand.executor
        logs = TestInstanceCreator.spyLogger.logs
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
    }

    @After
    fun tearDown() {
        nepian.acceptInvitation()
        minene.acceptInvitation()
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }
        minene.logger.logs.forEachIndexed { i, s -> println("[Minene] $i -> $s") }

        assertThat(TestInstanceCreator.tearDown(), `is`(true))
    }

    @Test
    fun デフォルトホームへの招待を送ると送信メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You invited Minene to your default home"))
    }

    @Test
    fun デフォルトホームへの招待を受けると招待メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(minene.lastMsg(), `is`(buildString {
            append("[Homes] You have been invited from Nepian to default home.\n")
            append("To accept an invitation, please run /home invite within 30 seconds")
        }))
    }

    @Test
    fun 名前付きホームへの招待他を送ると送信メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You invited Minene to your home named <home1>"))
    }

    @Test
    fun 名前付きホームへの招待を受けると招待メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(minene.lastMsg(), `is`(buildString {
            append("[Homes] You have been invited from Nepian to home named <home1>.\n")
            append("To accept an invitation, please run /home invite within 30 seconds")
        }))
    }

    @Test
    fun デフォルトホームへ招待を受けたプレイヤーは招待メタデータにスレッドを持っている() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(minene.hasMetadata("homes.invite"), `is`(true))
        assertThat(minene.getMetadata("homes.invite")[0].value() is Thread, `is`(true))
    }

    @Test
    fun 名前付きホームへ招待を受けたプレイヤーは招待メータデータにスレッドを持っている() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(minene.hasMetadata("homes.invite"), `is`(true))
        assertThat(minene.getMetadata("homes.invite")[0].value() is Thread, `is`(true))
    }

    @Test
    fun デフォルトホームへの招待を受け入れるとそのホームへ移動する() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 名前付きホームへの招待を受け入れるとそのホームへ移動する() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun デフォルトホームへの招待を受け入れると招待メタデータが削除される() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.hasMetadata("homes.invite"), `is`(false))
    }

    @Test
    fun 名前付きホームへの招待を受け入れると招待メタデータが削除される() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.hasMetadata("homes.invite"), `is`(false))
    }

    @Test
    fun デフォルトホームへの招待を受け入れるとスレッドが終了される() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).let {
            command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

            assertThat(it.isAlive, `is`(false))
        }
    }

    @Test
    fun 名前付きホームへの招待を受け入れるとスレッドが終了される() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).let {
            command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

            assertThat(it.isAlive, `is`(false))
        }
    }

    @Test
    fun デフォルトホームが設定されていない状態で招待を送った場合に失敗メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete"))
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Nepian's default home does not exist"))
    }

    @Test
    fun 名前付きホームが設定されていない状態で招待を送った場合に失敗メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("delete", "home1"))
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Nepian's home named <home1> does not exist"))
    }

    @Test
    fun デフォルトホームへの招待は三十秒後に自動的に拒否される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        System.nanoTime().let {
            (minene.getMetadata("homes.invite")[0].value() as Thread).join()
            ((System.nanoTime() - it) / 1000000).let {
                mockServer.logger.info("経過時間: $it ms")

                assertThat("三十秒後にスレッドが終了していない", it in 29000..31000, `is`(true))
            }
        }
    }

    @Test
    fun 名前付きホームへの招待は三十秒後に自動的に拒否される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        System.nanoTime().let {
            (minene.getMetadata("homes.invite")[0].value() as Thread).join()
            ((System.nanoTime() - it) / 1000000).let {
                mockServer.logger.info("経過時間: $it ms")

                assertThat("三十秒後にスレッドが終了していない", it in 29000..31000, `is`(true))
            }
        }
    }

    @Test
    fun デフォルホームへの招待が拒否された場合に招待したプレイヤーへ拒否メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).join()

        assertThat(nepian.lastMsg(), `is`("[Homes] Minene canceled your invitation"))
    }

    @Test
    fun 名前付きホームへの招待が拒否された場合に招待したプレイヤーへ拒否メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).join()

        assertThat(nepian.lastMsg(), `is`("[Homes] Minene canceled your invitation"))
    }

    @Test
    fun デフォルホームへの招待が拒否された場合に招待されたプレイヤーへ拒否メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).join()

        assertThat(minene.lastMsg(), `is`("[Homes] Invitation from Nepian has been canceled"))
    }

    @Test
    fun 名前付きホームへの招待が拒否された場合に招待されたプレイヤーへ拒否メッセージが表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).join()

        assertThat(minene.lastMsg(), `is`("[Homes] Invitation from Nepian has been canceled"))
    }

    @Test
    fun デフォルホームへの招待が拒否された場合に招待されたプレイヤーの招待メタデータが削除される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).join()

        assertThat(minene.hasMetadata("homes.invite"), `is`(false))
    }

    @Test
    fun 名前付きホームへの招待が拒否された場合に招待されたプレイヤーの招待メタデータが削除される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        (minene.getMetadata("homes.invite")[0].value() as Thread).join()

        assertThat(minene.hasMetadata("homes.invite"), `is`(false))
    }

    @Test
    fun デフォルトホームがプライベートでも招待を行うことができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You invited Minene to your default home"))
        assertThat(minene.lastMsg(), `is`(buildString {
            append("[Homes] You have been invited from Nepian to default home.\n")
            append("To accept an invitation, please run /home invite within 30 seconds")
        }))
    }

    @Test
    fun 名前付きホームがプライベートでも招待を行うことができる() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You invited Minene to your home named <home1>"))
        assertThat(minene.lastMsg(), `is`(buildString {
            append("[Homes] You have been invited from Nepian to home named <home1>.\n")
            append("To accept an invitation, please run /home invite within 30 seconds")
        }))
    }

    @Test
    fun デフォルトホームがプライベートでも招待を受け入れることができる() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on"))
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 名前付きホームがプライベートでも招待を受け入れることができる() {
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("private", "on", "home1"))
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun デフォルトホームへの招待を受け入れるのに権限は必要ない() {
        minene.setOps(false)
        minene.set(Permission.HOME)
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 名前付きホームへの招待を受け入れるのに権限は必要ない() {
        minene.setOps(false)
        minene.set(Permission.HOME)
        minene.teleport(MockWorldFactory.makeRandomLocation())
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun デフォルトホームへの招待は親権限が必要() {
        nepian.setOps(false)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command>"))
    }

    @Test
    fun 名前付きホームへの招待は親権限が必要() {
        nepian.setOps(false)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command>"))
    }

    @Test
    fun デフォルトホームへの招待は招待権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.invite>"))
    }

    @Test
    fun 名前付きホームへの招待は招待権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.invite>"))
    }

    @Test
    fun 名前付きホームへの招待は名前付き招待権限が必要() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_INVITE)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You don't have permission <homes.command.invite.name>"))
    }

    @Test
    fun 招待権限を持っているとデフォルトホームへ招待ができる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_INVITE)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(nepian.lastMsg(), `is`(not("[Homes] You don't have permission <homes.command.invite>")))
    }

    @Test
    fun 名前付き招待権限を持っていると名前付きホームへ招待ができる() {
        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_INVITE, Permission.HOME_INVITE_NAME)
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`(not("[Homes] You don't have permission <homes.command.invite.name>")))
    }

    @Test
    fun 招待機能が設定でオフの場合はデフォルトホームへの招待を行えない() {
        homes.configs.copy(onInvite = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Not allowed by the configuration of this server"))
    }

    @Test
    fun 招待機能が設定でオフの場合は名前付きホームへの招待を行えない() {
        homes.configs.copy(onInvite = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Not allowed by the configuration of this server"))
    }

    @Test
    fun 引数が間違っていた場合は使い方を表示する() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1", "home2"))

        assertThat(nepian.lastMsg(), `is`(buildString {
            append("invite command usage:\n")
            append("/home invite : Accept the invitation\n")
            append("/home invite <player_name> : Invite to your default home\n")
            append("/home invite <player_name> <home_name> : Invite to your named home")
        }))
    }

    @Test
    fun 招待したプレイヤーが存在しない場合はメッセージを表示する() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Moichi", "home1"))

        assertThat(nepian.lastMsg(), `is`("[Homes] Player <Moichi> does not exist"))
    }

    @Test
    fun 招待されていない状態に招待許可をすると招待がないと表示される() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite"))

        assertThat(nepian.lastMsg(), `is`("[Homes] You have not received an invitation"))
    }

    @Test
    fun 既に招待を受けているプレイヤーに招待を送った場合にメッセージを表示する() {
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(nepian.lastMsg(), `is`("Minene already has another invitation"))
    }
}
