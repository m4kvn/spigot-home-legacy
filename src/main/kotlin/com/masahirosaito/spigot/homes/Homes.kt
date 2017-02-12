package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.HomeCommand
import com.masahirosaito.spigot.mscore.Messenger
import com.masahirosaito.spigot.mscore.utils.load
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Homes : JavaPlugin() {
    lateinit var configs: Configs
    lateinit var messenger: Messenger
    lateinit var homeManager: HomeManager

    override fun onEnable() {
        configs = Configs.fromFile(File(dataFolder, "configs.json").load())
        messenger = Messenger(this, configs.onDebug)
        homeManager = HomeManager(this)

        getCommand("home").executor = HomeCommand(this)

        messenger.debug("[設定確認] $configs")
    }

    override fun onDisable() {
        homeManager.save()
    }
}
