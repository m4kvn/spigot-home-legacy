package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings
import org.bukkit.OfflinePlayer

class NamedHomePrivateException(player: OfflinePlayer, homeName: String) :
        HomesException(Strings.NAMED_HOME_IS_PRIVATE(player.name, homeName))
