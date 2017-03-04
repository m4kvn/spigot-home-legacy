package com.masahirosaito.spigot.homes.tests.exceptions

class CanNotFindOfflinePlayerException(playerName: String) : Exception("Player <$playerName> does not exist")
