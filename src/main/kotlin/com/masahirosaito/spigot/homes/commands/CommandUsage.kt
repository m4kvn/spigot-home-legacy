package com.masahirosaito.spigot.homes.commands

class CommandUsage(
    private val baseCommand: BaseCommand,
    val usage: List<Pair<String, String>>,
) {
    override fun toString(): String = buildString {
        append("&6${baseCommand.name} command usage:")
        usage.forEach { append("\n  &b${it.first}&r : ${it.second}") }
    }
}
