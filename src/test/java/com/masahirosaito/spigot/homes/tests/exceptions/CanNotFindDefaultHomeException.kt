package com.masahirosaito.spigot.homes.tests.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindDefaultHomeException(player: OfflinePlayer) : Exception("${player.name}'s default home does not exist")
