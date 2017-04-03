package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.strings.commands.*

object Strings {
    lateinit var homes: Homes
    val PLAYER_NAME = "[player-name]"
    val PERMISSION_NAME = "[permission-name]"
    val HOME_NAME = "[home-name]"
    val HOME_LIMIT_NUM = "[home-limit-num]"
    val COMMAND_NAME = "[command-name]"

    fun load(homes: Homes) {
        this.homes = homes

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
