package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_CONSOLE_COMMAND
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.CONSOLE_COMMAND_LIST

class NoConsoleCommandException :
        HomesException("${NO_CONSOLE_COMMAND()}\n${CONSOLE_COMMAND_LIST()}")
