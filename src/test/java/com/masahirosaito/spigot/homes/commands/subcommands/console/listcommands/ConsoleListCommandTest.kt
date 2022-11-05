package com.masahirosaito.spigot.homes.commands.subcommands.console.listcommands

import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.PLAYER_LIST
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homeConsoleCommandSender
import com.masahirosaito.spigot.homes.testutils.executeHomeCommand
import com.masahirosaito.spigot.homes.testutils.lastMsg
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class ConsoleListCommandTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コンソールからコマンドを実行した場合はプレイヤーリストを表示する() {
        homeConsoleCommandSender.executeHomeCommand("list")
        assertEquals(homeConsoleCommandSender.lastMsg(), PLAYER_LIST())
    }
}
