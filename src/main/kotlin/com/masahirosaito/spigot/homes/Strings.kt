package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.strings.ErrorStrings
import com.masahirosaito.spigot.homes.strings.HomeDisplayStrings
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings
import com.masahirosaito.spigot.homes.strings.commands.RemoveCommandStrings

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
                RemoveCommandStrings.load(this)
                InviteCommandStrings.load(this)
            }
        }
    }
}
