package com.masahirosaito.spigot.homes.tests.exceptions

import org.bukkit.OfflinePlayer

class CanNotFindPlayerHomeException(player: OfflinePlayer) : Exception("${player.name}'s home does not exist")
