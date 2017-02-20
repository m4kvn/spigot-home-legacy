package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.homes.homedata.HomeData
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Homes : JavaPlugin() {
    lateinit var configs: Configs
    lateinit var messenger: Messenger
    lateinit var homedata: HomeData
    lateinit var homedataFile: File

    override fun onEnable() {
        homedataFile = File(dataFolder, "homedata.json").load()
        configs = Configs.load(File(dataFolder, "configs.json").load())
        homedata = HomeData.load(homedataFile)
        messenger = Messenger(this, configs.onDebug)

        getCommand("home").executor = HomeCommand(this)

        messenger.debug("[設定確認] $configs")
    }

    override fun onDisable() {
        homedata.save(homedataFile)
    }

    private fun File.load(): File = this.apply {
        if (!parentFile.exists()) parentFile.mkdirs()
        if (!exists()) createNewFile()
    }
}
