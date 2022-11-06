package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.datas.FeeData
import com.masahirosaito.spigot.homes.listeners.*
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_ECONOMY
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_VAULT
import com.masahirosaito.spigot.homes.strings.Strings
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

class Homes : JavaPlugin {
    companion object { lateinit var homes: Homes }
    val fee: FeeData = loadFeeData()
    var econ: Economy? = null

    constructor() : super()

    constructor(
            loader: JavaPluginLoader,
            description: PluginDescriptionFile,
            dataFolder: File, file: File
    ) : super(loader, description, dataFolder, file)

    override fun onLoad() {
        super.onLoad()
        homes = this
        Configs.load()
        Strings.load()
    }

    override fun onEnable() {
        NMSManager.load()
        PlayerDataManager.load()
        UpdateChecker.checkUpdate()
        registerCommands()
        registerListeners()
        econ = loadEconomy()
    }

    override fun onDisable() {
        PlayerDataManager.save()
    }

    fun reload() {
        onDisable()
        onLoad()
        onEnable()
    }

    private fun loadFeeData() : FeeData {
        val file = File(dataFolder, "fee.json").load()
        return loadDataAndSave(file) { FeeData() }
    }

    private fun loadEconomy(): Economy? {
        if (server.pluginManager.getPlugin("Vault") == null) {
            Messenger.log(NO_VAULT)
            return null
        }
        server.servicesManager.getRegistration(Economy::class.java).let {
            if (it == null) {
                Messenger.log(NO_ECONOMY)
                return null
            }
            return it.provider
        }
    }

    private fun registerCommands() {
        getCommand("home")?.setExecutor(HomeCommand())
    }

    private fun registerListeners() {
        PlayerRespawnListener(this).register()
        PlayerJoinListener(this).register()
        ChunkLoadListener(this).register()
        ChunkUnLoadListener(this).register()
        PlayerMoveListener(this).register()
        EntityDamageListener(this).register()
    }
}
