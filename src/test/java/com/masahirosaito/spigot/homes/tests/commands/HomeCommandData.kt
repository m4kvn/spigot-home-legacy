package com.masahirosaito.spigot.homes.tests.commands

object HomeCommandData : CommandData {

    override fun name(): String = "home"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home" to "Teleport to your default home",
            "/home <home_name>" to "Teleport to your named home",
            "/home -p <player_name>" to "Teleport to player's default home",
            "/home <home_name> -p <player_name>" to "Teleport to player's named home"
    )

    override fun description(): String = "Homes Command"
}
