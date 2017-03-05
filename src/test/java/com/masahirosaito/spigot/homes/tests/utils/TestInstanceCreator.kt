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

    val pluginFolder = File("bin/test/server/plugins/homestest")
    val pluginFile = File(pluginFolder, "testPluginFile")
    val configFile = File(pluginFolder, "configs.json")

    fun setUp(): Boolean {
        try {
            TestInstanceCreator.mockServer = mock(Server::class.java).apply {
                PowerMockito.`when`(logger).thenReturn(SpyLogger(Logger.getLogger("Homes")))
                PowerMockito.`when`(pluginManager).thenReturn(TestInstanceCreator.createPluginManager())
                PowerMockito.`when`(consoleSender).thenReturn(HomesConsoleCommandSender(this))
            }
            TestInstanceCreator.homes = TestInstanceCreator.createHomes(mockServer).apply {
                PowerMockito.`when`(name).thenReturn("Homes")
                PowerMockito.`when`(dataFolder).thenReturn(TestInstanceCreator.pluginFolder)
                PowerMockito.`when`(getCommand("home")).thenReturn(TestInstanceCreator.createPluginCommand(this))
                PowerMockito.`when`(server).thenReturn(TestInstanceCreator.mockServer)
            }
            Bukkit.setServer(TestInstanceCreator.mockServer)

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

            TestInstanceCreator.homes.onLoad()
            TestInstanceCreator.homes.onEnable()

            return true

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun tearDown(): Boolean {
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

        TestInstanceCreator.homes.onDisable()
        FileUtil.delete(TestInstanceCreator.pluginFolder)

        return true
    }

    private fun createHomes(server: Server): Homes {
        return PowerMockito.spy(Homes(createJavaPluginLoader(server), createDescriptionFile(), pluginFolder, pluginFile))
    }

    private fun createJavaPluginLoader(server: Server): JavaPluginLoader {
        return PowerMock.createMock(JavaPluginLoader::class.java).apply {
            Whitebox.setInternalState(this, "server", server)
        }
    }

    private fun createDescriptionFile(): PluginDescriptionFile {
        return PowerMockito.spy(PluginDescriptionFile("Homes", "0.6", "com.masahirosaito.spigot.homes.Homes")).apply {
            PowerMockito.`when`(commands).thenReturn(mapOf("home" to mapOf()))
            PowerMockito.`when`(authors).thenReturn(listOf())
        }
    }

    private fun createPluginCommand(homes: Homes): PluginCommand {
        val constructor = PowerMock.constructor(PluginCommand::class.java, String::class.java, Plugin::class.java)
        return createMock(PluginCommand::class.java, ConstructorArgs(constructor, "home", homes))
    }

    private fun createPluginManager(): PluginManager {
        return mock(PluginManager::class.java)
    }
}
