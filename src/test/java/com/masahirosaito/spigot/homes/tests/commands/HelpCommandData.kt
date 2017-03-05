package com.masahirosaito.spigot.homes.tests.commands

object HelpCommandData : CommandData {

    override fun name(): String = "help"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home help" to "Display the list of Homes commands",
            "/home help <command_name>" to "Display the usage of Homes command"
    )

    override fun description(): String = "Homes Help Command"
}
