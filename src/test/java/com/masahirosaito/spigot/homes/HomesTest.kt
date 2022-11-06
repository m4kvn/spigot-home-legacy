package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.datas.FeeData
import com.masahirosaito.spigot.homes.exceptions.NoConsoleCommandException
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_RECEIVED_INVITATION
import com.masahirosaito.spigot.homes.strings.Strings.HOME_NAME
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_NAMED_HOME
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createReceiveDefaultHomeInvitationFrom
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createReceiveNamedHomeInvitationFrom
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createSendDefaultHomeInvitationTo
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.createSendNamedHomeInvitationTo
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_DEFAULT_HOME_PRIVATE
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_DEFAULT_HOME_PUBLIC
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.createSetNamedHomePrivateMessage
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.createSetNamedHomePublicMessage
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.SET_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.createSetNamedHomeMessage
import com.masahirosaito.spigot.homes.testutils.*
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.defaultLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.feeFile
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homeConsoleCommandSender
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.minene
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.namedLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.pluginFolder
import org.junit.Ignore
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

@Suppress("NonAsciiCharacters", "TestFunctionName")
class HomesTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun プラグインフォルダが生成されている() {
        assertTrue(pluginFolder.exists())
    }

    @Test
    fun 設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "configs.json").exists())
    }

    @Test
    fun データファイルが生成されている() {
        assertTrue(File(pluginFolder, "playerhomes.json").exists())
    }

    @Test
    fun 料金設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "fee.json").exists())
    }

    @Test
    fun 料金設定ファイルが読み込まれている() {
        assertEquals(homes.fee, loadDataAndSave(feeFile) { FeeData() })
    }

    @Ignore
    @Test
    fun 設定ファイルが読み込まれている() {
    }

    @Ignore
    @Test
    fun 言語ファイルが読み込まれている() {
    }

    @Ignore
    @Test
    fun メッセンジャーが読み込まれている() {
    }

    @Test
    fun 経済プラグインが読み込まれている() {
        assertNotNull(homes.econ)
    }

    @Test
    fun ホームコマンドが登録されている() {
        val actual = homes.getCommand("home")?.executor is HomeCommand
        assertTrue(actual)
    }

    @Test
    fun ホームへ移動するコマンドの実行ができる() {
        nepian.executeHomeCommand()
        nepian.getDelayThread()?.join()
        assertEquals(nepian.location, defaultLocation)
    }

    @Test
    fun 名前付きホームへ移動するコマンドの実行ができる() {
        nepian.executeHomeCommand("home1")
        nepian.getDelayThread()?.join()
        assertEquals(nepian.location, namedLocation)
    }

    @Test
    fun 他人のホームへ移動するコマンドの実行ができる() {
        minene.executeHomeCommand("-p", "Nepian")
        minene.getDelayThread()?.join()
        assertEquals(minene.location, defaultLocation)
    }

    @Test
    fun 他人の名前付きホームへ移動するコマンドの実行ができる() {
        minene.executeHomeCommand("home1", "-p", "Nepian")
        minene.getDelayThread()?.join()
        assertEquals(minene.location, namedLocation)
    }

    @Test
    fun ホームを設定するコマンドの実行ができる() {
        nepian.executeHomeCommand("set")
        assertEquals(nepian.lastMsg(), SET_DEFAULT_HOME)
    }

    @Test
    fun 名前付きホームを設定するコマンドの実行ができる() {
        nepian.executeHomeCommand("set", "home1")
        assertEquals(nepian.lastMsg(), createSetNamedHomeMessage("home1"))
    }

    @Test
    fun 設定されたホームを削除するコマンドの実行ができる() {
        nepian.executeHomeCommand("delete")
        assertEquals(nepian.lastMsg(), DELETE_DEFAULT_HOME)
    }

    @Test
    fun 設定された名前付きホームを削除するコマンドの実行ができる() {
        nepian.executeHomeCommand("delete", "home1")
        val expected = DELETE_NAMED_HOME.replace(HOME_NAME, "home1")
        val actual = nepian.lastMsg()
        assertEquals(actual, expected)
    }

    @Test
    fun 設定されたホームをプライベート化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "on")
        assertEquals(nepian.lastMsg(), SET_DEFAULT_HOME_PRIVATE)
    }

    @Test
    fun 設定された名前付きホームをプライベート化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "on", "home1")
        assertEquals(nepian.lastMsg(), createSetNamedHomePrivateMessage("home1"))
    }

    @Test
    fun 設定されたホームをパブリック化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "off")
        assertEquals(nepian.lastMsg(), SET_DEFAULT_HOME_PUBLIC)
    }

    @Test
    fun 設定された名前付きホームをパブリック化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "off", "home1")
        assertEquals(nepian.lastMsg(), createSetNamedHomePublicMessage("home1"))
    }

    @Test
    fun プレイヤーから設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("list"))
    }

    @Test
    fun プレイヤーから他の人の設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(minene.executeHomeCommand("list", "Nepian"))
    }

    @Test
    fun コンソールから他の人の設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(homeConsoleCommandSender.executeHomeCommand("list", "Nepian"))
    }

    @Test
    fun 他の人を自分のホームに招待するコマンドの実行ができる() {
        nepian.executeHomeCommand("invite", "Minene")
        assertEquals(nepian.lastMsg(), createSendDefaultHomeInvitationTo("Minene"))
        assertEquals(minene.lastMsg(), createReceiveDefaultHomeInvitationFrom("Nepian"))
        minene.acceptInvitation()
    }

    @Test
    fun 他の人を自分の名前付きホームに招待するコマンドの実行ができる() {
        nepian.executeHomeCommand("invite", "Minene", "home1")
        assertEquals(nepian.lastMsg(), createSendNamedHomeInvitationTo("Minene", "home1"))
        assertEquals(minene.lastMsg(), createReceiveNamedHomeInvitationFrom("Nepian", "home1"))
        minene.acceptInvitation()
    }

    @Test
    fun 他の人からの招待を受けるコマンドの実行ができる() {
        nepian.executeHomeCommand("invite")
        assertEquals(nepian.lastMsg(), NO_RECEIVED_INVITATION)
    }

    @Test
    fun プレイヤーからコマンドの説明一覧を表示するコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("help"))
    }

    @Test
    fun コンソールからコマンドの説明一覧を表示するコマンドの実行ができる() {
        assertTrue(homeConsoleCommandSender.executeHomeCommand("help"))
    }

    @Test
    fun コマンドの使い方を表示するコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("help", "home"))
    }

    @Test
    fun コンソールからプラグインをリロードするコマンドの実行ができる() {
        assertTrue(homeConsoleCommandSender.executeHomeCommand("reload"))
    }

    @Test
    fun プレイヤーからプラグインをリロードするコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("reload"))
    }

    @Test
    fun コンソールからホームコマンドを実行した場合にエラーを表示する() {
        homeConsoleCommandSender.executeHomeCommand()
        assertEquals(lastMsg(), NoConsoleCommandException().message)
    }
}
