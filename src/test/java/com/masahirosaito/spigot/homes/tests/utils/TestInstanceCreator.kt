package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPluginLoader
import org.easymock.ConstructorArgs
import org.junit.Assert
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.easymock.PowerMock
import org.powermock.api.easymock.PowerMock.createMock
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.doAnswer
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.reflect.Whitebox
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

object TestInstanceCreator {
    lateinit var mockServer: Server
    lateinit var homes: Homes
    lateinit var spyLogger: SpyLogger

    val pluginFolder = File("bin/test/server/plugins/homestest")
    val pluginFile = File(pluginFolder, "testPluginFile")
    val configFile = File(pluginFolder, "configs.json")

    fun setUp(): Boolean {
        try {
            mockServer = mock(Server::class.java).apply {
                PowerMockito.`when`(logger).thenReturn(SpyLogger(Logger.getLogger("Homes")))
                PowerMockito.`when`(pluginManager).thenReturn(createPluginManager())
                PowerMockito.`when`(consoleSender).thenReturn(HomesConsoleCommandSender(this))
            }
            homes = TestInstanceCreator.createHomes(mockServer).apply {
                PowerMockito.`when`(name).thenReturn("Homes")
                PowerMockito.`when`(dataFolder).thenReturn(pluginFolder)
                PowerMockito.`when`(getCommand("home")).thenReturn(createPluginCommand(this))
                PowerMockito.`when`(server).thenReturn(mockServer)
            }
            spyLogger = (mockServer.logger as SpyLogger)
            Bukkit.setServer(mockServer)

            PowerMockito.`when`(Bukkit.getOfflinePlayers()).thenAnswer {
                MockPlayerFactory.offlinePlayers.values.toTypedArray()
            }
            PowerMockito.`when`(Bukkit.getOnlinePlayers()).thenAnswer {
                MockPlayerFactory.players.values
            }
            PowerMockito.`when`(Bukkit.getOfflinePlayer(any(UUID::class.java))).thenAnswer { invocation ->
                MockPlayerFactory.offlinePlayers[invocation.getArgumentAt(0, UUID::class.java)]
            }
            PowerMockito.`when`(Bukkit.getPlayer(any(UUID::class.java))).thenAnswer { invocation ->
                MockPlayerFactory.players[invocation.getArgumentAt(0, UUID::class.java)]
            }
            PowerMockito.`when`(Bukkit.getWorld(any(UUID::class.java))).thenAnswer { invocation ->
                MockWorldFactory.worlds[invocation.getArgumentAt(0, UUID::class.java)]
            }

            homes.onLoad()
            homes.onEnable()

            return true

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun tearDown(): Boolean {
        spyLogger.logs.forEachIndexed { i, s -> println("$i -> $s") }
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

    private fun createHomes(server: Server) = PowerMockito.spy(
            Homes(createJavaPluginLoader(server), createDescriptionFile(), pluginFolder, pluginFile)
    )

    private fun createJavaPluginLoader(server: Server) = PowerMock.createMock(
            JavaPluginLoader::class.java).apply {
        Whitebox.setInternalState(this, "server", server)
    }

    private fun createDescriptionFile() = PowerMockito.spy(
            PluginDescriptionFile("Homes", "0.6", "com.masahirosaito.spigot.homes.Homes")).apply {
        PowerMockito.`when`(commands).thenReturn(mapOf("home" to mapOf()))
        PowerMockito.`when`(authors).thenReturn(listOf())
    }

    private fun createPluginCommand(homes: Homes) = createMock(PluginCommand::class.java, ConstructorArgs(
            PowerMock.constructor(PluginCommand::class.java, String::class.java, Plugin::class.java), "home", homes)
    )

    private fun createPluginManager() = mock(PluginManager::class.java)
}
