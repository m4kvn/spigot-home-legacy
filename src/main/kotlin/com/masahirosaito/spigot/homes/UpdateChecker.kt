package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import org.bukkit.plugin.java.JavaPlugin
import java.net.URL

class UpdateChecker(user: String, repo: String) {

    val url = URL("https://api.github.com/repos/$user/$repo/releases/latest")

    data class Latest(val tag_name: String = "", val html_url: String = "")

    fun sendVersionMessage(plugin: JavaPlugin) {
        Thread {
            val latest = getLatest(plugin)
            val latestVersion = latest.tag_name

            if (plugin.description.version != latestVersion) {
                plugin.logger.info("New version $latestVersion available!")
                plugin.logger.info("Download from => ${latest.html_url}")
            }
        }.start()
    }

    fun getLatest(plugin: JavaPlugin): Latest {
        return try {
            Gson().fromJson(url.openConnection().inputStream.bufferedReader().readLine(), Latest::class.java)
        } catch(e: Exception) {
            Latest(tag_name = plugin.description.version)
        }
    }
}