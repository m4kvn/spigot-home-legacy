package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.exceptions.CommandArgumentIncorrectException
import com.masahirosaito.spigot.homes.exceptions.NotAllowedByConfigException
import com.masahirosaito.spigot.homes.exceptions.NotHavePermissionException
import org.bukkit.ChatColor
import org.bukkit.entity.Player

interface SubCommand {
    val plugin: Homes

    fun name(): String

    fun permission(): String

    fun description(): String

    fun usages(): List<Pair<String, String>>

    fun configs(): List<Boolean> = listOf()

    fun options(): List<String> = listOf()

    fun isInValidArgs(args: List<String>): Boolean = false

    fun onCommand(player: Player, args: List<String>) {
        checkPermission(player, permission())
        checkConfig()
        checkArgs(args)
        execute(player, args)
    }

    fun execute(player: Player, args: List<String>)

    fun checkPermission(player: Player, permission: String) {
        if (!permission.isNullOrBlank() && !player.hasPermission(permission)) {
            throw NotHavePermissionException(permission)
        }
    }

    fun checkConfig(config: Boolean) {
        if (!config) throw NotAllowedByConfigException()
    }

    private fun checkConfig() {
        if (configs().contains(false)) throw NotAllowedByConfigException()
    }

    private fun checkArgs(args: List<String>) {
        if (isInValidArgs(args)) throw CommandArgumentIncorrectException(this)
    }

    fun send(player: Player, message: String) {
        if (!message.isNullOrBlank()) plugin.messenger.send(player, message)
    }

    fun usage(): String = buildString {
        append("${ChatColor.GOLD}${name()} command usage:")
        usages().forEach { append("\n${ChatColor.AQUA}${it.first}${ChatColor.RESET} : ${it.second}") }
    }

    fun List<String>.getOptionArg(option: String): String {
        try {
            return get(indexOf(option) + 1)
        } catch (e: Exception) {
            throw CommandArgumentIncorrectException(this@SubCommand)
        }
    }

    fun List<String>.hasOptions(): Boolean {
        options().forEach { if (contains(it)) return true }; return false
    }

    fun options(index: Int) = options()[index]
}
