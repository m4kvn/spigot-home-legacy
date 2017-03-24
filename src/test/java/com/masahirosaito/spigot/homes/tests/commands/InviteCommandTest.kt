package com.masahirosaito.spigot.homes.tests.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.Permission
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

    lateinit var nepianLocation: Location
    lateinit var mineneLocation: Location

    val metadata = "homes.invite"

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

        nepian.teleport(MockWorldFactory.makeRandomLocation())
        assertThat(nepian.location, `is`(not(namedLocation)))

        minene.teleport(MockWorldFactory.makeRandomLocation())
        assertThat(minene.location, `is`(not(namedLocation)))

        nepianLocation = nepian.location
        mineneLocation = minene.location
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
    fun デフォルトホームへの招待を受けたプレイヤーは招待メタデータとしてスレッドを持っている() {

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

        assertThat(minene.hasMetadata(metadata), `is`(true))
        assertThat(minene.getMetadata(metadata)[0].value() is Thread, `is`(true))
    }

    @Test
    fun 名前付きホームへの招待を受けたプレイヤーは招待メタデータを持っている() {

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

        assertThat(minene.hasMetadata(metadata), `is`(true))
        assertThat(minene.getMetadata(metadata)[0].value() is Thread, `is`(true))
    }

    @Test
    fun 引数が間違っている場合は使い方を表示し終わる() {

        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1", "home2"))

        assertThat(nepian.lastMsg(), `is`(buildString {
            append("[Homes] The argument is incorrect\n")
            append("invite command usage:\n")
            append("/home invite : Accept the invitation\n")
            append("/home invite <player_name> : Invite to your default home\n")
            append("/home invite <player_name> <home_name> : Invite to your named home")
        }))

        assertThat(minene.hasMetadata(metadata), `is`(false))
    }

    @Test
    fun コマンドの実行には親権限が必要() {

        nepian.setOps(false)

        "[Homes] You don't have permission <homes.command>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))
        }
    }

    @Test
    fun 招待を送るコマンドの実行には招待権限が必要() {

        nepian.setOps(false)
        nepian.set(Permission.HOME)

        "[Homes] You don't have permission <homes.command.invite>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))
        }
    }

    @Test
    fun 名前付きホームへの招待を送るコマンドの実行には名前付き招待権限が必要() {

        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_INVITE)

        "[Homes] You don't have permission <homes.command.invite.name>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata("homes.invite"), `is`(false))
        }
    }

    @Test
    fun 招待権限を持っている場合は招待を送るコマンドの実行を行える() {

        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_INVITE)

        "[Homes] You invited Minene to your default home".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付き招待権限を持っている場合は名前付きホームへの招待を送るコマンドの実行を行える() {

        nepian.setOps(false)
        nepian.set(Permission.HOME, Permission.HOME_INVITE, Permission.HOME_INVITE_NAME)

        "[Homes] You invited Minene to your home named <home1>".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun デフォルトホームへの招待を受けたプレイヤーは招待メッセージが表示される() {

        buildString {
            append("[Homes] You have been invited from Nepian to default home.\n")
            append("To accept an invitation, please run /home invite within 30 seconds")
        }.apply {
            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            assertThat(minene.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 名前付きホームへの招待を受けたプレイヤーは招待メッセージが表示される() {

        buildString {
            append("[Homes] You have been invited from Nepian to home named <home1>.\n")
            append("To accept an invitation, please run /home invite within 30 seconds")
        }.apply {
            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
            assertThat(minene.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 親権限があれば招待を受け入れることができる() {

        minene.setOps(false)
        minene.set(Permission.HOME)

        "[Homes] Minene accepted your invitation".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

            (minene.getMetadata(metadata)[0].value() as Thread).let {
                command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))
                assertThat(nepian.lastMsg(), `is`(this))
                assertThat(minene.location, `is`(defaultLocation))
                assertThat(minene.hasMetadata(metadata), `is`(false))
                assertThat(it.isAlive, `is`(false))
            }
        }

        "[Homes] You accepted Nepian's invitation".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

            (minene.getMetadata(metadata)[0].value() as Thread).let {
                command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))
                assertThat(minene.lastMsg(), `is`(this))
                assertThat(minene.location, `is`(namedLocation))
                assertThat(minene.hasMetadata(metadata), `is`(false))
                assertThat(it.isAlive, `is`(false))
            }
        }
    }

    @Test
    fun デフォルトホームが設定されていない状態で招待を送った場合にメッセージが表示され終了する() {

        homes.homeManager.findPlayerHome(nepian).defaultHomeData = null

        "[Homes] Nepian's default home does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))

            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))
        }
    }

    @Test
    fun 名前付きホームが設定されていない状態で招待を送った場合にメッセージが表示され終了する() {

        homes.homeManager.findPlayerHome(nepian).namedHomeData.removeAll { it.name == "home1" }

        "[Homes] Nepian's home named <home1> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))

            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))
        }
    }

    @Test
    fun ホームへの招待は三十秒後に自動的に拒否される() {

        "[Homes] Minene canceled your invitation".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            System.nanoTime().let {
                (minene.getMetadata(metadata)[0].value() as Thread).join()
                ((System.nanoTime() - it) / 1000000).let {
                    mockServer.logger.info("経過時間: $it ms")

                    assertThat(it in 29000..31000, `is`(true))
                }
            }

            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))
        }

        "[Homes] Invitation from Nepian has been canceled".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            System.nanoTime().let {
                (minene.getMetadata(metadata)[0].value() as Thread).join()
                ((System.nanoTime() - it) / 1000000).let {
                    mockServer.logger.info("経過時間: $it ms")

                    assertThat(it in 29000..31000, `is`(true))
                }
            }

            assertThat(minene.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))
        }
    }

    @Test
    fun ホームがプライベートでも招待を送ることができる() {

        homes.homeManager.findDefaultHome(nepian).isPrivate = true
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(defaultLocation))

        homes.homeManager.findNamedHome(nepian, "home1").isPrivate = true
        command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
        command.onCommand(minene, pluginCommand, "home", arrayOf("invite"))

        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun 招待機能が設定でオフの場合はデフォルトホームへの招待を行えない() {

        homes.configs.copy(onInvite = false).apply {
            save(TestInstanceCreator.configFile)
            homes.onDisable()
            homes.onEnable()
            assertThat(homes.configs, `is`(this))
        }

        "[Homes] Not allowed by the configuration of this server".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
            assertThat(minene.hasMetadata(metadata), `is`(false))
        }
    }

    @Test
    fun 招待したプレイヤーが存在しない場合はメッセージを表示する() {

        "[Homes] Player <Moichi> does not exist".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Moichi"))
            assertThat(nepian.lastMsg(), `is`(this))

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Moichi", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 招待されていない状態に招待許可をすると招待がないと表示される() {

        "[Homes] You have not received an invitation".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite"))
            assertThat(nepian.lastMsg(), `is`(this))
        }
    }

    @Test
    fun 既に招待を受けているプレイヤーに招待を送った場合にメッセージを表示する() {

        "[Homes] Minene already has another invitation".apply {

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene"))
            assertThat(nepian.lastMsg(), `is`(this))
            minene.acceptInvitation()

            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
            command.onCommand(nepian, pluginCommand, "home", arrayOf("invite", "Minene", "home1"))
            assertThat(nepian.lastMsg(), `is`(this))
            minene.acceptInvitation()
        }
    }
}
