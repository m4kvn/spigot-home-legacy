package com.masahirosaito.spigot.homes.commands.subcommands.reloadcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.ConsoleCommand
import org.bukkit.command.ConsoleCommandSender

class ReloadCommand(override val homes: Homes) : ConsoleCommand {
    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {

    }

    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val description: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val usage: CommandUsage
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val commands: List<BaseCommand>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun configs(): List<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValidArgs(args: List<String>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
