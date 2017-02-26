package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand

class CommandArgumentIncorrectException(subCommand: SubCommand) : Exception(
        "The argument is incorrect\n${subCommand.usage()}"
)
