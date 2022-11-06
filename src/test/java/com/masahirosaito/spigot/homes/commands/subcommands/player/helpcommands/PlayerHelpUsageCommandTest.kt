package com.masahirosaito.spigot.homes.commands.subcommands.player.helpcommands

import com.masahirosaito.spigot.homes.Permission.home_command_help
import com.masahirosaito.spigot.homes.Permission.home_command_help_command
import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.strings.ErrorStrings.createArgumentIncorrect
import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoPermissionMessage
import com.masahirosaito.spigot.homes.testutils.*
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class PlayerHelpUsageCommandTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun ヘルプコマンド権限を持っていない場合はコマンドを実行できない() {
        nepian.setOps(false)
        nepian.executeHomeCommand("help", "help")
        assertEquals(nepian.lastMsg(), createNoPermissionMessage(home_command_help))
    }

    @Test
    fun ヘルプコマンドコマンド権限を持っていない場合はコマンドを実行できない() {
        nepian.setOps(false)
        nepian.setPermissions(home_command_help)
        nepian.executeHomeCommand("help", "help")
        assertEquals(nepian.lastMsg(), createNoPermissionMessage(home_command_help_command))
    }

    @Test
    fun プレイヤーからコマンドを実行した場合はプレイヤーコマンドの使い方を表示する() {
        nepian.executeHomeCommand("help", "home")
        assertEquals(nepian.lastMsg(), HomeCommand().usage.toString())
    }

    @Test
    fun プレイヤーから実行されたコマンドの引数が間違っている場合は使い方を表示する() {
        nepian.executeHomeCommand("help", "home", "help")
        assertEquals(nepian.lastMsg(), createArgumentIncorrect(PlayerHelpCommand().usage.toString()))
    }
}
