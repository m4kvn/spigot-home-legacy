package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import com.masahirosaito.spigot.homes.listeners.PlayerRespawnListener
import com.masahirosaito.spigot.homes.oldhomedata.OldHomeData
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

class Homes : JavaPlugin {
    lateinit var configs: Configs
    lateinit var messenger: Messenger
    lateinit var homeManager: HomeManager
    lateinit var playerHomeDataFile: File

    constructor() : super()

    constructor(loader: JavaPluginLoader, description: PluginDescriptionFile, dataFolder: File, file: File) :
            super(loader, description, dataFolder, file)

    override fun onEnable() {
        configs = Configs.load(File(dataFolder, "configs.json").load())
        messenger = Messenger(this, configs.onDebug)
        loadData()
        getCommand("home").executor = HomeCommand(this)
        PlayerRespawnListener(this).register()
    }

    override fun onDisable() {
        homeManager.save(playerHomeDataFile)
    }

    private fun File.load(): File = this.apply {
        if (!parentFile.exists()) parentFile.mkdirs()
        if (!exists()) createNewFile()
    }

    private fun loadData() {
        playerHomeDataFile = File(dataFolder, "playerhomes.json")

        val oldHomeDataFile = File(dataFolder, "homedata.json")

        if (!oldHomeDataFile.exists() || playerHomeDataFile.exists()) {
            homeManager = HomeManager.load(playerHomeDataFile.load())
            return
        }

        homeManager = HomeManager().apply {
            OldHomeData.load(oldHomeDataFile).playerHomes.forEach {
                val uuid = it.key
                playerHomes.put(uuid, PlayerHome().apply {
                    it.value.defaultHome?.let { defaultHomeData = HomeData(uuid, "default", it) }
                    it.value.namedHomes.forEach { namedHomeData.add(HomeData(uuid, it.key, it.value)) }
                })
            }
            save(playerHomeDataFile)
        }

        oldHomeDataFile.delete()
    }
}
