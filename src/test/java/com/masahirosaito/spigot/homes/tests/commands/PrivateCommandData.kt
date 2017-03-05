package com.masahirosaito.spigot.homes.tests.commands

object PrivateCommandData : CommandData {

    override fun name(): String = "private"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home private (on/off)" to "Set your default home private or public",
            "/home private (on/off) <home_name>" to "Set your named home private or public"
    )

    override fun description(): String = "Set your home private or public"

    fun msgSuccessPrivate(isPrivate: Boolean, name: String? = null): String = prefix(buildString {
        append("Set your ")
        append(if (name == null) "default home " else "home named $name ")
        append(if (isPrivate) "PRIVATE" else "PUBLIC")
    })
}
