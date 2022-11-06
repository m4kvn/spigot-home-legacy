package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createAlreadyHasInvitation
import org.bukkit.OfflinePlayer

class AlreadyHasInvitationException(player: OfflinePlayer) :
    HomesException(createAlreadyHasInvitation(player.name))
