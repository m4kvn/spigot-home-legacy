package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindPlayerHomeException(player: OfflinePlayer) : HomesException("${player.name}'s home does not exist")
