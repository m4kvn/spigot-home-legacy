package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.homes.listeners.PlayerRespawnListener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Homes : JavaPlugin() {
    lateinit var configs: Configs
    lateinit var messenger: Messenger
    lateinit var homeManager: HomeManager
    lateinit var homedataFile: File

    override fun onEnable() {
        homedataFile = File(dataFolder, "homedata.json").load()
        configs = Configs.load(File(dataFolder, "configs.json").load())
        homeManager = HomeManager.load(homedataFile)
        messenger = Messenger(this, configs.onDebug)

        getCommand("home").executor = HomeCommand(this)

        PlayerRespawnListener(this).register()
    }

    override fun onDisable() {
        homeManager.save(homedataFile)
    }

    private fun File.load(): File = this.apply {
        if (!parentFile.exists()) parentFile.mkdirs()
        if (!exists()) createNewFile()
    }
}
