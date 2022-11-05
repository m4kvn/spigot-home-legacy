package com.masahirosaito.spigot.homes.testutils

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.PlayerDataManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import org.junit.Assert
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.mockito.internal.util.MockUtil.createMock
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import org.mockito.Mockito.`when` as pwhen

object TestInstanceCreator {
    private lateinit var mockServer: Server
    lateinit var homes: Homes
    private lateinit var javaPluginLoader: JavaPluginLoader
    private lateinit var vault: JavaPlugin
    private lateinit var mockServicesManager: ServicesManager
    private lateinit var mockPluginManager: PluginManager
    lateinit var homeConsoleCommandSender: HomesConsoleCommandSender
    private lateinit var registeredServiceProvider: RegisteredServiceProvider<Economy>
    lateinit var pluginCommand: PluginCommand

    lateinit var economy: Economy
    lateinit var spyLogger: SpyLogger
    lateinit var nepian: Player
    lateinit var minene: Player
    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location
    lateinit var command: CommandExecutor

    val pluginFolder = File("bin/test/server/plugins/homestest")
    private val pluginFile = File(pluginFolder, "testPluginFile")

    val feeFile = File(pluginFolder, "fee.json")

    const val NAMED_HOME: String = "home1"

    fun setUp(): Boolean {
        refresh()
        economy = MyEconomy()
        spyLogger = SpyLogger(Logger.getLogger("Homes"))

        try {
            mockPluginManager = mock(PluginManager::class.java)
            mockServicesManager = mock(ServicesManager::class.java)
            mockServer = mock(Server::class.java).apply {
                homeConsoleCommandSender = HomesConsoleCommandSender(this)
                pwhen(logger).thenReturn(spyLogger)
                pwhen(pluginManager).thenReturn(mockPluginManager)
                pwhen(consoleSender).thenReturn(homeConsoleCommandSender)
                pwhen(servicesManager).thenReturn(mockServicesManager)
            }
            javaPluginLoader = createMock(withSettings().build(JavaPluginLoader::class.java)).apply {
                val serverField = javaClass.getDeclaredField("server")
                serverField.isAccessible = true
                serverField.set(this, mockServer)
            }
            vault = mock(JavaPlugin::class.java)
            homes = spy(Homes(javaPluginLoader, createHomesDescriptionFile(), pluginFolder, pluginFile)).apply {
                val pluginCommandConstructor = PluginCommand::class.java
                    .getDeclaredConstructor(String::class.java, Plugin::class.java)
                pluginCommandConstructor.isAccessible = true
                pluginCommand = spy(pluginCommandConstructor.newInstance("home", this))
                pwhen(name).thenReturn("Homes")
                pwhen(dataFolder).thenReturn(pluginFolder)
                pwhen(getCommand("home")).thenReturn(pluginCommand)
                pwhen(server).thenReturn(mockServer)
            }
            registeredServiceProvider = spy(
                RegisteredServiceProvider<Economy>(
                    economy.javaClass, economy, ServicePriority.Normal, vault
                )
            )

            doReturn(registeredServiceProvider).`when`(mockServicesManager).getRegistration(Economy::class.java)
            doReturn(vault).`when`(mockPluginManager).getPlugin("Vault")

            Bukkit.setServer(mockServer)

            pwhen(Bukkit.getOfflinePlayers()).thenAnswer {
                MockPlayerFactory.offlinePlayers.values.toTypedArray()
            }
            pwhen(Bukkit.getOnlinePlayers()).thenAnswer {
                MockPlayerFactory.players.values
            }
            pwhen(Bukkit.getOfflinePlayer(any(UUID::class.java))).thenAnswer { invocation ->
                MockPlayerFactory.offlinePlayers[invocation.getArgument(0)]
            }
            pwhen(Bukkit.getPlayer(any(UUID::class.java))).thenAnswer { invocation ->
                MockPlayerFactory.players[invocation.getArgument(0)]
            }
            pwhen(Bukkit.getWorld(any(UUID::class.java))).thenAnswer { invocation ->
                MockWorldFactory.worlds[invocation.getArgument(0)]
            }

            homes.onLoad()

            Configs.onUpdateCheck = false

            homes.onEnable()

            command = pluginCommand.executor

            nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", homes)
            nepian.setOps()

            minene = MockPlayerFactory.makeNewMockPlayer("Minene", homes)
            minene.setOps()

            PlayerDataManager.setDefaultHome(nepian, nepian.location)
            PlayerDataManager.setNamedHome(nepian, nepian.location, NAMED_HOME)
            defaultLocation = PlayerDataManager.findDefaultHome(nepian).location
            namedLocation = PlayerDataManager.findNamedHome(nepian, NAMED_HOME).location

            Assert.assertEquals(defaultLocation, nepian.location)
            Assert.assertEquals(namedLocation, nepian.location)

            while (defaultLocation == nepian.location || namedLocation == nepian.location) {
                nepian.randomTeleport()
            }

            while (defaultLocation == minene.location || namedLocation == minene.location) {
                minene.randomTeleport()
            }

            homes.econ?.createPlayerAccount(nepian)
            homes.econ?.depositPlayer(nepian, 5000.0)

            return true

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun tearDown(): Boolean {
        nepian.logger.logs.forEachIndexed { i, s -> println("[Nepian] $i -> $s") }
        minene.logger.logs.forEachIndexed { i, s -> println("[Minene] $i -> $s") }
        spyLogger.logs.forEachIndexed { i, s -> println("[Server] $i -> $s") }

        killThreads("homes.invite")
        killThreads("homes.delay")

        (economy as MyEconomy).clear()

        try {
            Bukkit::class.java.getDeclaredField("server").let {
                it.isAccessible = true
                it.set(Class.forName("org.bukkit.Bukkit"), null)
            }
        } catch (e: Exception) {
            Logger.getLogger("Homes").log(
                Level.SEVERE,
                "Error while trying to unregister the server from Bukkit. Has Bukkit changed?"
            )
            e.printStackTrace()
            Assert.fail(e.message)
            return false
        }

        homes.onDisable()
        refresh()

        return true
    }

    private fun refresh() {
        MockPlayerFactory.clear()
        MockWorldFactory.clear()
        FileUtil.delete(pluginFolder)
    }

    private fun createHomesDescriptionFile() = spy(
        PluginDescriptionFile(
            "Homes", "test-version", "com.masahirosaito.spigot.homes.Homes"
        )
    ).apply {
        pwhen(commands).thenReturn(mapOf("home" to mapOf()))
        pwhen(authors).thenReturn(listOf())
    }

    private fun killThread(player: Player, meta: String) {
        if (player.hasMetadata(meta)) {
            (player.getMetadata(meta).first().value() as Thread).run {
                if (isAlive) {
                    interrupt()
                    join()
                }
            }
        }
    }

    private fun killThreads(meta: String) {
        killThread(nepian, meta)
        killThread(minene, meta)
    }
}
