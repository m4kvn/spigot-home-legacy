package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings
import org.bukkit.OfflinePlayer

class DefaultHomePrivateException(player: OfflinePlayer) :
        HomesException(Strings.DEFAULT_HOME_IS_PRIVATE(player.name))
