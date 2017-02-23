package com.masahirosaito.spigot.homes.exceptions

class NoSuchCommandException(commandName: String) : Exception("Command <$commandName> does not exist")
