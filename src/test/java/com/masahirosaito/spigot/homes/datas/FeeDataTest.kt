package com.masahirosaito.spigot.homes.datas

import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import com.masahirosaito.spigot.homes.saveData
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.feeFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class FeeDataTest {

    @Test
    fun 設定ファイルを読みこむことができる() {
        feeFile.load()
        saveData(feeFile, FeeData(HOME = 30.0))
        assertEquals(loadData(feeFile, FeeData::class.java).HOME, 30.0, 0.0)
        feeFile.delete()
    }
}
