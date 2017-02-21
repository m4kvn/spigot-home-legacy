package com.masahirosaito.spigot.homes.exceptions

class NotHavePermissionException(permission: String) : Exception("You don't have permission <$permission>")