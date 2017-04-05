package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.strings.ErrorStrings.ARGUMENT_INCORRECT

class ArgumentIncorrectException(commandUsage: CommandUsage) :
        HomesException(ARGUMENT_INCORRECT(commandUsage.toString()))
