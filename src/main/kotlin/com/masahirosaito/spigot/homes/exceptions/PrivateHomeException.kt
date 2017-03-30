package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings
import org.bukkit.OfflinePlayer

class PrivateHomeException(offlinePlayer: OfflinePlayer, homeName: String? = null):
        HomesException(Strings.PRIVATE_HOME(offlinePlayer.name, homeName))
