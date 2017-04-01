package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import java.net.URL
import kotlin.concurrent.thread

object UpdateChecker {
    var isUpdate: Boolean = false
    lateinit var updateMessage: String
    lateinit var urlMessage: String

    fun checkUpdate(homes: Homes) {
        if (!Configs.onUpdateCheck) return
        thread {
            try {
                val conn = URL("https://api.curseforge.com/servermods/files?projectids=261377")
                        .openConnection().apply {
                    addRequestProperty("User-Agent", "Homes Update Checker")
                    readTimeout = 5000
                    doOutput = true
                }
                val versionList = Gson()
                        .fromJson(conn.getInputStream().bufferedReader().readLine(), Array<Version>::class.java)

                if (versionList.isNotEmpty() && !versionList.last().fileName.contains(homes.description.version)) {
                    isUpdate = true
                    updateMessage = "Stable version: ${versionList.last().name} is out!" +
                            " You are still running version: ${homes.description.version}"
                    urlMessage = "Update at: https://dev.bukkit.org/projects/homes-teleportation-plugin"

                    Messenger.log(updateMessage)
                    Messenger.log(urlMessage)
                }
            } catch (e: Exception) {
            }
        }
    }

    data class Version(
            val downloadUrl: String = "",
            val fileName: String = "",
            val fileUrl: String = "",
            val gameVersion: String = "",
            val md5: String = "",
            val name: String = "",
            val projectId: Long = 0,
            val releaseType: String = ""
    )
}
