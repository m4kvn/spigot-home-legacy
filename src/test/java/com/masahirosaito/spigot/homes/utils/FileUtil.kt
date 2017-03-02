package com.masahirosaito.spigot.homes.utils

import java.io.File

object FileUtil {
    fun delete(file: File) {
        if (!file.exists()) return
        if (file.isDirectory) file.listFiles().forEach { delete(it) }
        file.delete()
    }
}
