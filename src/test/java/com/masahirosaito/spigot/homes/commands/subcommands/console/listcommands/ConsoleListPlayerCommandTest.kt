package com.masahirosaito.spigot.homes.commands.subcommands.console.listcommands

import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.strings.ErrorStrings.createArgumentIncorrect
import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoHome
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homeConsoleCommandSender
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.executeHomeCommand
import com.masahirosaito.spigot.homes.testutils.lastMsg
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class ConsoleListPlayerCommandTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun コンソールから実行したコマンドの引数が間違っていた場合に使い方を表示する() {
        homeConsoleCommandSender.executeHomeCommand("list", "Nepian", "Minene")
        assertEquals(lastMsg(), createArgumentIncorrect(ConsoleListCommand().usage.toString()))
    }

    @Test
    fun コンソールから実行した場合はプライベートホームも表示できる() {
        PlayerDataManager.apply {
            setDefaultHomePrivate(nepian, true)
            setNamedHomePrivate(nepian, "home1", true)
        }
        homeConsoleCommandSender.executeHomeCommand("list", "Nepian")
        assertNotEquals(lastMsg(), createNoHome(nepian.name))
    }
}
