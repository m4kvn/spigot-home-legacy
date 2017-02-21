package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindPlayerHomeException(player: OfflinePlayer) : Exception("${player.name}'s home does not exist")