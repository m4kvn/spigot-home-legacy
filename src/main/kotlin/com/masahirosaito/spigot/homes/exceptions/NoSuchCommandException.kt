package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings

class NoSuchCommandException(commandName: String) :
        HomesException(Strings.NO_COMMAND(commandName))
