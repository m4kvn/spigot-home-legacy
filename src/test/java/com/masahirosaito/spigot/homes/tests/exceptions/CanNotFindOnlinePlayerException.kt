package com.masahirosaito.spigot.homes.tests.exceptions

class CanNotFindOnlinePlayerException(playerName: String) : Exception("Player <$playerName> does not exist")
