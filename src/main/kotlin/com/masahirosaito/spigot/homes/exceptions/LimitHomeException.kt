package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createHomeLimitMessage

class LimitHomeException(limit: Int) :
    HomesException(createHomeLimitMessage(limit))
