package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.strings.ErrorStrings
import com.masahirosaito.spigot.homes.strings.HomeDisplayStrings
import com.masahirosaito.spigot.homes.strings.commands.*

object Strings {
    val PLAYER_NAME = "[player-name]"
    val PERMISSION_NAME = "[permission-name]"
    val HOME_NAME = "[home-name]"
    val HOME_LIMIT_NUM = "[home-limit-num]"
    val COMMAND_NAME = "[command-name]"

    fun load(homes: Homes) {
        "${homes.dataFolder}/languages/${Configs.language}".apply {
            ErrorStrings.load(this)
            HomeDisplayStrings.load(this)

            "$this/commands".apply {
                HomeCommandStrings.load(this)
                DeleteCommandStrings.load(this)
                InviteCommandStrings.load(this)
                HelpCommandStrings.load(this)
                PrivateCommandStrings.load(this)
                SetCommandStrings.load(this)
                ListCommandStrings.load(this)
            }
        }
    }
}
