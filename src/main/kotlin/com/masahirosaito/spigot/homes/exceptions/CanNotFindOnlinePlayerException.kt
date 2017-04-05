package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_ONLINE_PLAYER

class CanNotFindOnlinePlayerException(playerName: String) :
        HomesException(NO_ONLINE_PLAYER(playerName))
