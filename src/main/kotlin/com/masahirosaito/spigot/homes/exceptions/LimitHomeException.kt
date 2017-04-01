package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings

class LimitHomeException(limit: Int) :
        HomesException(Strings.LIMIT_HOME(limit))
