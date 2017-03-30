package com.masahirosaito.spigot.homes

import org.bukkit.ChatColor

object Strings {

    fun NO_DEFAULT_HOME(playerName: String)=
            "$playerName's default home does not exist"

    fun NO_NAMED_HOME(playerName: String, homeName: String) =
            "$playerName's home named <$homeName> does not exist"

    fun LIMIT_HOME(limit: Int) =
            "You can not set more homes (Limit: ${ChatColor.RESET}$limit${ChatColor.RED})"

    fun HOME_DISPLAY_NAME(playerName: String, homeName: String? = null) =
            "$playerName's\n${if (homeName == null) "default home" else "home named\n<$homeName>"}"

    fun PRIVATE_HOME(playerName: String, homeName: String? = null) =
            "$playerName's ${if (homeName == null) "default home" else "home named <$homeName>"} is PRIVATE"

    fun REMOVE_DEFAULT_HOME() =
            "${ChatColor.AQUA}Successfully delete your default home${ChatColor.RESET}"

    fun REMOVE_NAMED_HOME(homeName: String) =
            "${ChatColor.AQUA}Successfully delete your named home" +
            " <${ChatColor.RESET}$homeName${ChatColor.AQUA}>${ChatColor.RESET}"

    fun NO_COMMAND(commandName: String) =
            "Command <$commandName> does not exist"

    fun NO_PERMISSION(permission: String) =
            "You don't have permission <$permission>"
}
