package com.masahirosaito.spigot.homes.commands

import org.bukkit.ChatColor

class CommandUsage(val baseCommand: BaseCommand, val usage: List<Pair<String, String>>) {

    override fun toString(): String = buildString {
        append("${ChatColor.GOLD}${baseCommand.name} command usage:")
        usage.forEach { append("\n${ChatColor.AQUA}${it.first}${ChatColor.RESET} : ${it.second}") }
    }
}
