package com.masahirosaito.spigot.homes.tests.utils

import java.io.File

object FileUtil {
    fun delete(file: File) {
        if (!file.exists()) return
        if (file.isDirectory) file.listFiles().forEach { FileUtil.delete(it) }
        file.delete()
    }
}
