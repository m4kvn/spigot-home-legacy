package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.datas.FeeData
import com.masahirosaito.spigot.homes.listeners.ChunkLoadListener
import com.masahirosaito.spigot.homes.listeners.ChunkUnLoadListener
import com.masahirosaito.spigot.homes.listeners.PlayerJoinListener
import com.masahirosaito.spigot.homes.listeners.PlayerRespawnListener
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

class Homes : JavaPlugin {
    val fee: FeeData = FeeData.load(File(dataFolder, "fee.json").load())
    var econ: Economy? = null

    constructor() : super()

    constructor(
            loader: JavaPluginLoader,
            description: PluginDescriptionFile,
            dataFolder: File, file: File
    ) : super(loader, description, dataFolder, file)

    override fun onEnable() {
        Configs.load(this)
        Messenger.load(this)
        PlayerDataManager.load(this)

        getCommand("home").executor = HomeCommand(this)
        PlayerRespawnListener(this).register()
        PlayerJoinListener(this).register()
        ChunkLoadListener(this).register()
        ChunkUnLoadListener(this).register()
        UpdateChecker.checkUpdate(this)

        econ = loadEconomy()
    }

    override fun onDisable() {
        PlayerDataManager.save()
    }

    private fun loadEconomy(): Economy? {
        if (server.pluginManager.getPlugin("Vault") == null) {
            Messenger.log("Fee function stopped because Vault can not be found.")
            return null
        }
        server.servicesManager.getRegistration(Economy::class.java).let {
            if (it == null) {
                Messenger.log("Fee function stopped because the Economy plugin can not be found.")
            }
            return it.provider
        }
    }
}
