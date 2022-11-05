package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.exceptions.NoNamedHomeException
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.NAMED_HOME
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import org.bukkit.OfflinePlayer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class PlayerDataManagerTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
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
