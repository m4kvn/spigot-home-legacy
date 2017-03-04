package com.masahirosaito.spigot.homes.tests.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindNamedHomeException(player: OfflinePlayer, name: String) : Exception(
        "${player.name}'s named home <$name> does not exist"
)
