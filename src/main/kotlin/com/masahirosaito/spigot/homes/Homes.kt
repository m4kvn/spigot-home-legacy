package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.mscore.Messenger
import org.bukkit.plugin.java.JavaPlugin

class Homes : JavaPlugin() {
    lateinit var messenger: Messenger
    lateinit var homeManager: HomeManager

    override fun onEnable() {
        messenger = Messenger(this, true)
        homeManager = HomeManager(this)

        getCommand("home").executor = HomeCommand(this)
    }

    override fun onDisable() {
        homeManager.save()
    }
}
