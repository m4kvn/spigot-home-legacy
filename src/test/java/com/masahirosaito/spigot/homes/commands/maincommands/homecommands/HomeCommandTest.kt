package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.strings.EconomyStrings.createNotEnoughMoneyErrorMessage
import com.masahirosaito.spigot.homes.testutils.*
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.economy
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class HomeCommandTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun テレポートがキャンセルされた場合はお金を消費しない() {
        homes.fee.HOME = 1000.0
        val money: Double = economy.getBalance(nepian)
        nepian.executeHomeCommand()
        nepian.cancelTeleport()
        nepian.getDelayThread()?.join()
        assertEquals(economy.getBalance(nepian), money, 0.0)
    }

    @Test
    fun テレポートが成功した場合はお金を消費する() {
        homes.fee.HOME = 1000.0
        val money: Double = economy.getBalance(nepian)
        nepian.executeHomeCommand()
        nepian.getDelayThread()?.join()
        assertEquals(economy.getBalance(nepian), money - 1000.0, 0.0)
    }

    @Test
    fun お金が足りなかった場合はテレポートに失敗する() {
        homes.fee.HOME = 1000.0
        homes.econ?.withdrawPlayer(nepian, 4500.0)
        assertTrue(economy.getBalance(nepian) < 1000.0)
        nepian.executeHomeCommand()
        nepian.getDelayThread()?.join()
        assertEquals(nepian.lastMsg(), createNotEnoughMoneyErrorMessage(homes.fee.HOME))
    }
}
