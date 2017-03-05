package com.masahirosaito.spigot.homes.tests.commands

object ListCommandData : CommandData {
    override fun name(): String = "list"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home list" to "Display the list of homes",
            "/home list <player_name>" to "Display the list of player's homes"
    )
}
