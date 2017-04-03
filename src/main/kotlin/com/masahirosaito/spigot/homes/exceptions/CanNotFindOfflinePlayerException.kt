package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_OFFLINE_PLAYER

class CanNotFindOfflinePlayerException(playerName: String) :
        HomesException(NO_OFFLINE_PLAYER(playerName))
