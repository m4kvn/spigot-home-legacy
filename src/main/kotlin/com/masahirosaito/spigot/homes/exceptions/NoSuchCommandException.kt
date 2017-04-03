package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_COMMAND

class NoSuchCommandException(commandName: String) :
        HomesException(NO_COMMAND(commandName))
