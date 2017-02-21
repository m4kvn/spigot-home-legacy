package com.masahirosaito.spigot.homes.exceptions

class CanNotFindOfflinePlayerException(playerName: String) : Exception("Player <$playerName> does not exist")