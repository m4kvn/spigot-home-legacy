package com.masahirosaito.spigot.homes.tests.commands

object SetCommandData : CommandData {

    override fun name(): String = "set"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home set" to "Set your location to your default home",
            "/home set <home_name>" to "Set your location to your named home"
    )

    override fun description(): String = "Set your home or named home"

    fun msgSuccessSetDefaultHome() = prefix("Successfully set as default home")

    fun msgSuccessSetNamedHome(name: String) = prefix("Successfully set as home named <$name>")
}
