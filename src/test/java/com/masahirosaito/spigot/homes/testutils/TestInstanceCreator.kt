package com.masahirosaito.spigot.homes.testutils

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
import org.easymock.ConstructorArgs
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.mockito.Matchers.any
import org.powermock.api.easymock.PowerMock
import org.powermock.api.easymock.PowerMock.createMock
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.*
import org.powermock.api.support.membermodification.MemberMatcher.constructor
import org.powermock.reflect.Whitebox
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import org.powermock.api.mockito.PowerMockito.`when` as pwhen

object TestInstanceCreator {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var javaPluginLoader: JavaPluginLoader
    lateinit var vault: JavaPlugin
    lateinit var mockServicesManager: ServicesManager
    lateinit var mockPluginManager: PluginManager
    lateinit var homeConsoleCommandSender: HomesConsoleCommandSender
    lateinit var registeredServiceProvider: RegisteredServiceProvider<Economy>
    lateinit var pluginCommand: PluginCommand

    lateinit var economy: Economy
    lateinit var spyLogger: SpyLogger
    lateinit var nepian: Player
    lateinit var minene: Player
    lateinit var defaultLocation: Location
    lateinit var namedLocation: Location
    lateinit var command: CommandExecutor

    val pluginFolder = File("bin/test/server/plugins/homestest")
    val pluginFile = File(pluginFolder, "testPluginFile")
    val configFile = File(pluginFolder, "configs.json")
    val playerhomesFile = File(pluginFolder, "playerhomes.json")
    val feeFile = File(pluginFolder, "fee.json")

    fun setUp(): Boolean {
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
            javaPluginLoader = PowerMock.createMock(JavaPluginLoader::class.java).apply {
                Whitebox.setInternalState(this, "server", mockServer)
            }
            vault = mock(JavaPlugin::class.java)
            homes = spy(Homes(javaPluginLoader, createHomesDescriptionFile(), pluginFolder, pluginFile)).apply {
                pluginCommand = createMock(PluginCommand::class.java, ConstructorArgs(
                        constructor(PluginCommand::class.java, String::class.java, Plugin::class.java), "home", this))
                pwhen(name).thenReturn("Homes")
                pwhen(dataFolder).thenReturn(pluginFolder)
                pwhen(getCommand("home")).thenReturn(pluginCommand)
                pwhen(server).thenReturn(mockServer)
            }
            registeredServiceProvider = spy(RegisteredServiceProvider<Economy>(
                    economy.javaClass, economy, ServicePriority.Normal, vault)
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
                MockPlayerFactory.offlinePlayers[invocation.getArgumentAt(0, UUID::class.java)]
            }
            pwhen(Bukkit.getPlayer(any(UUID::class.java))).thenAnswer { invocation ->
                MockPlayerFactory.players[invocation.getArgumentAt(0, UUID::class.java)]
            }
            pwhen(Bukkit.getWorld(any(UUID::class.java))).thenAnswer { invocation ->
                MockWorldFactory.worlds[invocation.getArgumentAt(0, UUID::class.java)]
            }

            homes.onLoad()
            homes.onEnable()

            command = pluginCommand.executor

            nepian = MockPlayerFactory.makeNewMockPlayer("Nepian", homes)
            nepian.setOps()

            minene = MockPlayerFactory.makeNewMockPlayer("Minene", homes)
            minene.setOps()

            PlayerDataManager.setDefaultHome(nepian, nepian.location)
            PlayerDataManager.setNamedHome(nepian, nepian.location, "home1")
            defaultLocation = PlayerDataManager.findDefaultHome(nepian).location
            namedLocation = PlayerDataManager.findNamedHome(nepian, "home1").location

            Assert.assertThat(defaultLocation, `is`(nepian.location))
            Assert.assertThat(namedLocation, `is`(nepian.location))

            while (defaultLocation == nepian.location || namedLocation == nepian.location) {
                nepian.randomTeleport()
            }

            while (defaultLocation == minene.location || namedLocation == minene.location) {
                minene.randomTeleport()
            }

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
        MockPlayerFactory.clear()
        MockWorldFactory.clear()

        try {
            Bukkit::class.java.getDeclaredField("server").let {
                it.isAccessible = true
                it.set(Class.forName("org.bukkit.Bukkit"), null)
            }
        } catch (e: Exception) {
            Logger.getLogger("Homes").log(Level.SEVERE,
                    "Error while trying to unregister the server from Bukkit. Has Bukkit changed?")
            e.printStackTrace()
            Assert.fail(e.message)
            return false
        }

        homes.onDisable()
        FileUtil.delete(pluginFolder)

        return true
    }

    private fun createHomesDescriptionFile() = PowerMockito.spy(PluginDescriptionFile(
            "Homes", "test-version", "com.masahirosaito.spigot.homes.Homes")).apply {
        PowerMockito.`when`(commands).thenReturn(mapOf("home" to mapOf()))
        PowerMockito.`when`(authors).thenReturn(listOf())
    }
}
