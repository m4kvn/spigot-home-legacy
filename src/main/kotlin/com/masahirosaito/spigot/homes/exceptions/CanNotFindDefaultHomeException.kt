package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindDefaultHomeException(val player: OfflinePlayer) : Exception()