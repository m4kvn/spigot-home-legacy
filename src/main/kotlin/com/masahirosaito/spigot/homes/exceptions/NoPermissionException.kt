package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_PERMISSION

class NoPermissionException(permission: String) :
        HomesException(NO_PERMISSION(permission))
