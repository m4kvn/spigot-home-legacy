package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings

class NoPermissionException(permission: String) :
        HomesException(Strings.NO_PERMISSION(permission))
