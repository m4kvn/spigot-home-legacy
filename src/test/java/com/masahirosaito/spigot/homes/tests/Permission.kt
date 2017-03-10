package com.masahirosaito.spigot.homes.tests

enum class Permission(val permission: String) {

    HOME("homes.command"),
    HOME_NAME("homes.command.name"),
    HOME_PLAYER("homes.command.player"),
    HOME_PLAYER_NAME("homes.command.player.name"),

    HOME_SET("homes.command.set"),
    HOME_SET_NAME("homes.command.set.name"),

    HOME_DELETE("homes.command.delete"),
    HOME_DELETE_NAME("homes.command.delete.name"),

    HOME_INVITE("homes.command.invite"),
    HOME_INVITE_NAME("homes.command.invite.name"),

    HOME_PRIVATE("homes.command.private"),
    HOME_PRIVATE_NAME("homes.command.private.name"),

    HOME_HELP("homes.command.help"),
    HOME_HELP_COMMAND("homes.command.help.command")
}
