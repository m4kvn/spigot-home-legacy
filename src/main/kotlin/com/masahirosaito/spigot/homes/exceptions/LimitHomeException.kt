package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.HOME_LIMIT

class LimitHomeException(limit: Int) :
        HomesException(HOME_LIMIT(limit))
