package com.masahirosaito.spigot.homes.commands.subcommands.console.helpcommands

import com.masahirosaito.spigot.homes.strings.ErrorStrings.ARGUMENT_INCORRECT
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_COMMAND
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
class ConsoleHelpUsageCommandTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コンソールからコマンドを実行した場合はコンソールコマンド以外の使い方は表示できない() {
        homeConsoleCommandSender.executeHomeCommand("help", "home")
        assertEquals(lastMsg(), NO_COMMAND("home"))
    }

    @Test
    fun コンソールからコマンドを実行した場合はコンソールコマンドの使い方を表示する() {
        homeConsoleCommandSender.executeHomeCommand("help", "help")
        assertEquals(lastMsg(), ConsoleHelpCommand().usage.toString())
    }

    @Test
    fun コンソールから実行されたコマンドの引数が間違っている場合は使い方を表示する() {
        homeConsoleCommandSender.executeHomeCommand("help", "home", "help")
        assertEquals(lastMsg(), ARGUMENT_INCORRECT(ConsoleHelpCommand().usage.toString()))
    }
}
