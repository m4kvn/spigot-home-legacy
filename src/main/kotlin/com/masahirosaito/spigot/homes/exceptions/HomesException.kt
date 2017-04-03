package com.masahirosaito.spigot.homes.exceptions

open class HomesException(msg: String) : Exception(msg) {

    fun getColorMsg(): String = "&c$message&r"
}
