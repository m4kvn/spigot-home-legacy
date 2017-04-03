package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings
import org.bukkit.OfflinePlayer

class AlreadyHasInvitationException(player: OfflinePlayer) :
        HomesException(Strings.INVITE_ALREADY_HAS_INVITATION(player.name))
