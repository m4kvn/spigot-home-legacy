package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.DEFAULT_HOME_IS_PRIVATE
import org.bukkit.OfflinePlayer

class DefaultHomePrivateException(player: OfflinePlayer) :
        HomesException(DEFAULT_HOME_IS_PRIVATE(player.name))
