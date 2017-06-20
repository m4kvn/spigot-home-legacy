package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.exceptions.NoNamedHomeException
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.NAMED_HOME
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import org.bukkit.*
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.java.JavaPluginLoader
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
        PluginManager::class, ServicesManager::class, RegisteredServiceProvider::class)
class PlayerDataManagerTest {
    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun デフォルトホームを探索できる() {
        assertEquals(nepian as OfflinePlayer, PlayerDataManager.findDefaultHome(nepian).offlinePlayer)
    }

    @Test
    fun 名前付きホームの存在確認ができる() {
        assertTrue(PlayerDataManager.hasNamedHome(nepian, NAMED_HOME))
    }

    @Test
    fun デフォルトホームをセットできる() {
        PlayerDataManager.setDefaultHome(nepian, nepian.location)
        assertEquals(nepian.location, PlayerDataManager.findDefaultHome(nepian).location)
    }

    @Test
    fun 名前付きホームを削除できる() {
        PlayerDataManager.removeNamedHome(nepian, NAMED_HOME)
        assertFalse(PlayerDataManager.hasNamedHome(nepian, NAMED_HOME))
    }

    @Test
    fun 設定でホーム数制限が０未満の場合に名前付きホームを設定できない() {
        PlayerDataManager.removeNamedHome(nepian, NAMED_HOME)
        assertFalse(PlayerDataManager.hasNamedHome(nepian, NAMED_HOME))

        Configs.homeLimit = -1
        assertEquals(-1, Configs.homeLimit)

        try {
            PlayerDataManager.setNamedHome(nepian, nepian.location, NAMED_HOME)
        } catch(e: NoNamedHomeException) {
            assertFalse(PlayerDataManager.hasNamedHome(nepian, NAMED_HOME))
        }

        Configs.homeLimit = -2
        assertEquals(-2, Configs.homeLimit)

        try {
            PlayerDataManager.setNamedHome(nepian, nepian.location, NAMED_HOME)
        } catch(e: NoNamedHomeException) {
            assertFalse(PlayerDataManager.hasNamedHome(nepian, NAMED_HOME))
        }
    }
}
