package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindDefaultHomeException(player: OfflinePlayer) : HomesException("${player.name}'s default home does not exist")
