package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.datas.strings.EconomyStringData
import com.masahirosaito.spigot.homes.load
import com.masahirosaito.spigot.homes.loadDataAndSave
import com.masahirosaito.spigot.homes.strings.Strings.BALANCE
import com.masahirosaito.spigot.homes.strings.Strings.COMMAND_FEE
import com.masahirosaito.spigot.homes.strings.Strings.ERROR_MESSAGE
import com.masahirosaito.spigot.homes.strings.Strings.PAY_AMOUNT
import java.io.File

object EconomyStrings {
    private const val FILE_NAME = "economy.json"
    private lateinit var data: EconomyStringData

    val NO_ACCOUNT_ERROR: String get() = data.NO_ACCOUNT_ERROR

    fun load(folderPath: String) {
        val file = File(folderPath, FILE_NAME).load()
        data = loadDataAndSave(file) { EconomyStringData() }
    }

    fun createPayMessage(payAmount: String, balance: String) =
        data.PAY
            .replace(PAY_AMOUNT, payAmount)
            .replace(BALANCE, balance)

    fun createEconomyErrorMessage(errorMessage: String) =
        data.ECONOMY_ERROR
            .replace(ERROR_MESSAGE, errorMessage)

    fun createNotEnoughMoneyErrorMessage(commandFee: Double) =
        data.NOT_ENOUGH_MONEY_ERROR
            .replace(COMMAND_FEE, commandFee.toString())
}
