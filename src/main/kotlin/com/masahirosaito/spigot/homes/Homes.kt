package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.HomeCommand
import org.bukkit.plugin.java.JavaPlugin

class Homes : JavaPlugin() {
    lateinit var homeManager: HomeManager

    override fun onEnable() {
        homeManager = HomeManager(this)

        getCommand("home").executor = HomeCommand(this)
    }

    override fun onDisable() {
        homeManager.save()
    }
}
