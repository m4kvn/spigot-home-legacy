package com.masahirosaito.spigot.homes.exceptions

class CanNotFindOnlinePlayerException(playerName: String) : HomesException("Player <$playerName> does not exist")
