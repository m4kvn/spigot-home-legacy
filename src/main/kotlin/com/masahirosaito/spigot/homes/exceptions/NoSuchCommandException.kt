package com.masahirosaito.spigot.homes.exceptions

class NoSuchCommandException(commandName: String) : HomesException("Command <$commandName> does not exist")
