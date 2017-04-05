package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.ALREADY_HAS_INVITATION
import org.bukkit.OfflinePlayer

class AlreadyHasInvitationException(player: OfflinePlayer) :
        HomesException(ALREADY_HAS_INVITATION(player.name))
