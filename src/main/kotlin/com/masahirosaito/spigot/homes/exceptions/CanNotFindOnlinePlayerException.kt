package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoOnlinePlayerMessage

class CanNotFindOnlinePlayerException(playerName: String) :
    HomesException(createNoOnlinePlayerMessage(playerName))
