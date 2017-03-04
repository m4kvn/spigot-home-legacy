package com.masahirosaito.spigot.homes.tests.exceptions

import com.masahirosaito.spigot.homes.tests.commands.CommandData

class CommandArgumentIncorrectException(command: CommandData) : Exception(
        "The argument is incorrect\n${command.usage()}"
)
