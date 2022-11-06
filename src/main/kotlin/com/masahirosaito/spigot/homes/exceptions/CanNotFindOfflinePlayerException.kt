package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoOfflinePlayerMessage

class CanNotFindOfflinePlayerException(playerName: String) :
    HomesException(createNoOfflinePlayerMessage(playerName))
