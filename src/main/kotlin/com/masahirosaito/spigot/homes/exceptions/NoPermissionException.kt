package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoPermissionMessage

class NoPermissionException(permission: String) : HomesException(createNoPermissionMessage(permission))
