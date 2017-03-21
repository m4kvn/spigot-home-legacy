package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindNamedHomeException(player: OfflinePlayer, name: String) : HomesException(
        "${player.name}'s home named <$name> does not exist"
)
