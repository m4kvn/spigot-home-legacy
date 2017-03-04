package com.masahirosaito.spigot.homes.tests.exceptions

class NoSuchCommandException(commandName: String) : Exception("Command <$commandName> does not exist")
