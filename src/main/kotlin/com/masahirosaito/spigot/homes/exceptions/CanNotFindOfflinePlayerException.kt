package com.masahirosaito.spigot.homes.exceptions

class CanNotFindOfflinePlayerException(playerName: String) : HomesException("Player <$playerName> does not exist")
