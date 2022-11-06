package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoCommandMessage

class NoSuchCommandException(commandName: String) :
    HomesException(createNoCommandMessage(commandName))
