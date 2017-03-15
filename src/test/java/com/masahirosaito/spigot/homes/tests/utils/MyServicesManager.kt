package com.masahirosaito.spigot.homes.tests.utils

import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.java.JavaPlugin

class MyServicesManager(val economy: Economy, val plugin: JavaPlugin) : ServicesManager {

    override fun <T : Any?> register(service: Class<T>?, provider: T, plugin: Plugin?, priority: ServicePriority?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRegistrations(plugin: Plugin?): MutableList<RegisteredServiceProvider<*>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> getRegistrations(service: Class<T>?): MutableCollection<RegisteredServiceProvider<T>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregister(service: Class<*>?, provider: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregister(provider: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> getRegistration(service: Class<T>?): RegisteredServiceProvider<T> {
        return RegisteredServiceProvider<T>(service, service?.newInstance(), ServicePriority.Normal, plugin)
    }

    override fun unregisterAll(plugin: Plugin?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> isProvidedFor(service: Class<T>?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKnownServices(): MutableCollection<Class<*>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> load(service: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
