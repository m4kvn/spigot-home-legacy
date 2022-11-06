package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.strings.ErrorStrings.createArgumentIncorrect

class ArgumentIncorrectException(commandUsage: CommandUsage) :
    HomesException(createArgumentIncorrect(commandUsage.toString()))
