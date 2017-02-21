package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindPlayerHomeException(val player: OfflinePlayer) : Exception()