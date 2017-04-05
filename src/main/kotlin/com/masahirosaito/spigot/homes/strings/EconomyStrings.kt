package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.datas.strings.EconomyStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadData
import com.masahirosaito.spigot.homes.strings.Strings.BALANCE
import com.masahirosaito.spigot.homes.strings.Strings.COMMAND_FEE
import com.masahirosaito.spigot.homes.strings.Strings.ERROR_MESSAGE
import com.masahirosaito.spigot.homes.strings.Strings.PAY_AMOUNT
import java.io.File

object EconomyStrings {
    lateinit private var data: EconomyStringData

    fun load(folderPath: String) {
        data = loadData(File(folderPath, "economy.json").load(), EconomyStringData::class.java)
    }

    fun PAY(payAmount: String, balance: String) =
            data.PAY
                    .replace(PAY_AMOUNT, payAmount)
                    .replace(BALANCE, balance)

    fun ECONOMY_ERROR(errorMessage: String) =
            data.ECONOMY_ERROR
                    .replace(ERROR_MESSAGE, errorMessage)

    fun NO_ACCOUNT_ERROR() =
            data.NO_ACCOUNT_ERROR

    fun NOT_ENOUGH_MONEY_ERROR(commandFee: Double) =
            data.NOT_ENOUGH_MONEY_ERROR
                    .replace(COMMAND_FEE, commandFee.toString())
}
