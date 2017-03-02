package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.utils.FileUtil
import com.masahirosaito.spigot.homes.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPluginLoader
import org.easymock.ConstructorArgs
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.easymock.PowerMock
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.*
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.io.File

@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class, Server::class, PluginCommand::class)
class HomesTest {
    lateinit var mockServer: Server
    lateinit var homes: Homes

    val pluginFolder = File("bin/test/server/plugins/homestest")
    val pluginFile = File(pluginFolder, "testPluginFile")

    @Before
    fun setUp() {
        mockServer = mock(Server::class.java).apply {
            `when`(logger).thenReturn(Util.logger)
            `when`(pluginManager).thenReturn(createPluginManager())
        }
        homes = createHomes(mockServer).apply {
            `when`(name).thenReturn("Homes")
            `when`(dataFolder).thenReturn(pluginFolder)
            `when`(getCommand("home")).thenReturn(createPluginCommand(this))
            `when`(server).thenReturn(mockServer)
        }
        Bukkit.setServer(mockServer)
    }

    @After
    fun tearDown() {
        Bukkit::class.java.getDeclaredField("server").let {
            it.isAccessible = true
            it.set(Class.forName("org.bukkit.Bukkit"), null)
        }
        homes.onDisable()
        FileUtil.delete(pluginFolder)
    }

    @Test
    fun onEnableTest() {
        homes.onEnable()
    }

    fun createHomes(server: Server): Homes {
        return spy(Homes(createJavaPluginLoader(server), createDescriptionFile(), pluginFolder, pluginFile))
    }

    fun createJavaPluginLoader(server: Server): JavaPluginLoader {
        return PowerMock.createMock(JavaPluginLoader::class.java).apply {
            Whitebox.setInternalState(this, "server", server)
        }
    }

    fun createDescriptionFile(): PluginDescriptionFile {
        return spy(PluginDescriptionFile("Homes", "0.6", "com.masahirosaito.spigot.homes.Homes")).apply {
            `when`(commands).thenReturn(mapOf("home" to mapOf()))
            `when`(authors).thenReturn(listOf())
        }
    }

    fun createPluginCommand(homes: Homes): PluginCommand {
        val constructor = PowerMock.constructor(PluginCommand::class.java, String::class.java, Plugin::class.java)
        return PowerMock.createMock(PluginCommand::class.java, ConstructorArgs(constructor, "home", homes))
    }

    fun createPluginManager(): PluginManager {
        return PowerMockito.mock(PluginManager::class.java)
    }
}
