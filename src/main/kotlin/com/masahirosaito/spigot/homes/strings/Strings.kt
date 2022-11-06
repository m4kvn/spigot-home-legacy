package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.strings.commands.*

object Strings {
    const val PLAYER_NAME = "[player-name]"
    const val PERMISSION_NAME = "[permission-name]"
    const val HOME_NAME = "[home-name]"
    const val HOME_LIMIT_NUM = "[home-limit-num]"
    const val COMMAND_NAME = "[command-name]"
    const val COMMAND_USAGE = "[command-usage]"
    const val PAY_AMOUNT = "[pay-amount]"
    const val BALANCE = "[balance]"
    const val ERROR_MESSAGE = "[error-message]"
    const val COMMAND_FEE = "[command-fee]"
    const val DELAY = "[delay]"

    fun load() {

        "${homes.dataFolder}/languages/${Configs.language}".apply {
            ErrorStrings.load(this)
            HomeDisplayStrings.load(this)
            EconomyStrings.load(this)
            TeleportStrings.load(this)

            "$this/commands".apply {
                HomeCommandStrings.load(this)
                DeleteCommandStrings.load(this)
                InviteCommandStrings.load(this)
                HelpCommandStrings.load(this)
                PrivateCommandStrings.load(this)
                SetCommandStrings.load(this)
                ListCommandStrings.load(this)
                ReloadCommandStrings.load(this)
            }
        }
    }
}
