package com.masahirosaito.spigot.homes.tests.commands

object PrivateCommandData : CommandData {

    override fun name(): String = "private"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home private (on/off)" to "Set your default home private or public",
            "/home private (on/off) <home_name>" to "Set your named home private or public"
    )
}
