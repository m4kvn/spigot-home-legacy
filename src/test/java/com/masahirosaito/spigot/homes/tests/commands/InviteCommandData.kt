package com.masahirosaito.spigot.homes.tests.commands

object InviteCommandData : CommandData {

    override fun name(): String = "invite"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home invite" to "Accept the invitation",
            "/home invite <player_name>" to "Invite to your default home",
            "/home invite <player_name> <home_name>" to "Invite to your named home"
    )
}
