package com.masahirosaito.spigot.homes.exceptions

class NotHavePermissionException(permission: String) : HomesException("You don't have permission <$permission>")
