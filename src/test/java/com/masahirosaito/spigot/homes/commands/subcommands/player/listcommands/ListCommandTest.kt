package com.masahirosaito.spigot.homes.commands.subcommands.player.listcommands

import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.strings.EconomyStrings.NOT_ENOUGH_MONEY_ERROR
import com.masahirosaito.spigot.homes.strings.EconomyStrings.PAY
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.HOME_LIST
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.executeHomeCommand
import com.masahirosaito.spigot.homes.testutils.lastMsg
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class ListCommandTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コマンドを実行した場合にホームリストを表示する() {
        nepian.executeHomeCommand("list")
        assertEquals(nepian.lastMsg(), HOME_LIST(PlayerDataManager.findPlayerData(nepian), false))
    }

    @Test
    fun 料金が設定されたコマンドの実行時に所持金が足りなかった場合はエラーを表示する() {
        homes.fee.LIST = 10000.0
        nepian.executeHomeCommand("list")
        assertEquals(nepian.lastMsg(), NOT_ENOUGH_MONEY_ERROR(homes.fee.LIST))
    }

    @Test
    fun 料金が設定されたコマンドの実行時に所持金が足りている場合は所持金を消費しコマンドを実行する() {
        homes.fee.LIST = 1000.0
        nepian.executeHomeCommand("list")
        assertEquals(nepian.lastMsg(), PAY(1000.0.toString(), 4000.0.toString()))
    }
}
