package com.masahirosaito.spigot.homes.datas

import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import com.masahirosaito.spigot.homes.saveData
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.feeFile
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class FeeDataTest {

    @Test
    fun 設定ファイルを読みこむことができる() {
        feeFile.load()
        saveData(feeFile, FeeData(HOME = 30.0))
        assertThat(loadData(feeFile, FeeData::class.java).HOME, `is`(30.0))
        feeFile.delete()
    }
}
