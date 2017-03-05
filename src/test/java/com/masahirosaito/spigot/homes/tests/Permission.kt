package com.masahirosaito.spigot.homes.tests

enum class Permission(val permission: String) {

    HOME_DEFAULT("homes.command"),
    HOME_NAME("homes.command.name"),
    HOME_PLAYER("homes.command.player"),
    HOME_PLAYER_NAME("homes.command.player.name"),

    HOME_SET("homes.command.set"),
    HOME_SET_NAME("homes.command.set.name")
}
