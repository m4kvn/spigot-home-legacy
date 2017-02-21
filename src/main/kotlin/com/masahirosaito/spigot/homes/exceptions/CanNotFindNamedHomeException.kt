package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindNamedHomeException(val player: OfflinePlayer, val name: String) : Exception()