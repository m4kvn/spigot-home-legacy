package com.masahirosaito.spigot.homes.tests.commands

import org.bukkit.ChatColor

interface CommandData {

    fun name(): String

    fun usages(): List<Pair<String, String>>

    fun description(): String

    fun msg(e: Exception) = prefix(ChatColor.stripColor(e.message))

    fun usage(): String = buildString {
        append("${name()} command usage:")
        usages().forEach { append("\n${it.first} : ${it.second}") }
    }

    fun prefix(msg: String) = "[Homes] $msg"
}
