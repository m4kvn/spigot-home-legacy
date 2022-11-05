package com.masahirosaito.spigot.homes.commands.subcommands.player.listcommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission.home_admin
import com.masahirosaito.spigot.homes.Permission.home_command_list
import com.masahirosaito.spigot.homes.Permission.home_command_list_player
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.strings.ErrorStrings.ARGUMENT_INCORRECT
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NOT_ALLOW_BY_CONFIG
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_HOME
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_PERMISSION
import com.masahirosaito.spigot.homes.testutils.*
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.minene
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class PlayerListPlayerCommandTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun リストコマンド権限を持っていない場合はコマンドの実行ができない() {
        minene.setOps(false)
        minene.executeHomeCommand("list", "Nepian")
        assertEquals(minene.lastMsg(), NO_PERMISSION(home_command_list))
    }

    @Test
    fun リストプレイヤーコマンド権限を持っていない場合はコマンドの実行ができない() {
        minene.setOps(false)
        minene.setPermissions(home_command_list)
        minene.executeHomeCommand("list", "Nepian")
        assertEquals(minene.lastMsg(), NO_PERMISSION(home_command_list_player))
    }

    @Test
    fun プレイヤーホーム設定がオフの場合はコマンドの実行ができない() {
        Configs.onFriendHome = false
        minene.executeHomeCommand("list", "Nepian")
        assertEquals(minene.lastMsg(), NOT_ALLOW_BY_CONFIG())
    }

    @Test
    fun プレイヤーから実行したコマンドの引数が間違っていた場合に使い方を表示する() {
        minene.executeHomeCommand("list", "Nepian", "Minene")
        assertEquals(minene.lastMsg(), ARGUMENT_INCORRECT(PlayerListCommand().usage.toString()))
    }

    @Test
    fun 管理者権限を持っていない場合は他人のプライベートホームは表示できない() {
        PlayerDataManager.apply {
            setDefaultHomePrivate(nepian, true)
            setNamedHomePrivate(nepian, "home1", true)
        }
        minene.setOps(false)
        minene.setPermissions(home_command_list, home_command_list_player)
        minene.executeHomeCommand("list", "Nepian")
        assertEquals(minene.lastMsg(), NO_HOME(nepian.name))
    }

    @Test
    fun 管理者権限を持っている場合は他人のプライベートホームも表示できる() {
        PlayerDataManager.apply {
            setDefaultHomePrivate(nepian, true)
            setNamedHomePrivate(nepian, "home1", true)
        }
        minene.setOps(false)
        minene.setPermissions(home_admin)
        minene.executeHomeCommand("list", "Nepian")
        assertNotEquals(minene.lastMsg(), NO_HOME(nepian.name))
    }
}
