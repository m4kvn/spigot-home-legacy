package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_HOME
import org.bukkit.OfflinePlayer

class NoHomeException(offlinePlayer: OfflinePlayer) :
        HomesException(NO_HOME(offlinePlayer.name))
