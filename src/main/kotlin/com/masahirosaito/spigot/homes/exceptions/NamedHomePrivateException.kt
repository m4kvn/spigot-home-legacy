package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NAMED_HOME_IS_PRIVATE
import org.bukkit.OfflinePlayer

class NamedHomePrivateException(player: OfflinePlayer, homeName: String) :
        HomesException(NAMED_HOME_IS_PRIVATE(player.name, homeName))
