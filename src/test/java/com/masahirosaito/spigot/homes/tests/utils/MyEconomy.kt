package com.masahirosaito.spigot.homes.tests.utils

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*

class MyEconomy : Economy {
    val myBanks: MutableList<MyBank> = mutableListOf()
    val myPlayers: MutableList<MyPlayer> = mutableListOf()

    override fun getBanks(): MutableList<String> {
        return myBanks.map { it.name }.toMutableList()
    }

    override fun getBalance(playerName: String?): Double {
        return getBalance(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun getBalance(offlinePlayer: OfflinePlayer?): Double {
        return myPlayers.find { it.uuid == offlinePlayer?.uniqueId }!!.balance
    }

    override fun getBalance(playerName: String?, worldName: String?): Double {
        return getBalance(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun getBalance(offlinePlayer: OfflinePlayer?, worldName: String?): Double {
        return getBalance(offlinePlayer)
    }

    override fun getName(): String = "Homes Economy"

    override fun isBankOwner(bankName: String?, playerName: String?): EconomyResponse {
        return isBankOwner(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun isBankOwner(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        if (offlinePlayer == null) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Player is null")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(0.0, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Bank is null")

        if (bank.owner == offlinePlayer.uniqueId) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "You are not bank owner")
        } else {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.SUCCESS, null)
        }
    }

    override fun has(playerName: String?, amount: Double): Boolean {
        return has(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun has(offlinePlayer: OfflinePlayer?, amount: Double): Boolean {
        return getBalance(offlinePlayer) - amount >= 0
    }

    override fun has(playerName: String?, worldName: String?, amount: Double): Boolean {
        return has(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun has(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): Boolean {
        return has(offlinePlayer, amount)
    }

    override fun bankDeposit(bankName: String?, amount: Double): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(0.0, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Bank is null")

        if (amount < 0) {
            return EconomyResponse(amount, bank.balance,
                    EconomyResponse.ResponseType.FAILURE,
                    "Please enter a value of 0 or more")
        }

        bank.balance += amount

        return EconomyResponse(amount, bank.balance,
                EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun bankWithdraw(bankName: String?, amount: Double): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(0.0, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Bank is null")

        if (amount < 0) {
            return EconomyResponse(amount, bank.balance,
                    EconomyResponse.ResponseType.FAILURE,
                    "Please enter a value of 0 or more")
        }

        bank.balance -= amount

        return EconomyResponse(amount, bank.balance,
                EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun deleteBank(bankName: String?): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(0.0, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Bank is null")

        myBanks.remove(bank)

        return EconomyResponse(0.0, bank.balance,
                EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun depositPlayer(offlinePlayer: OfflinePlayer?, amount: Double): EconomyResponse {

        if (offlinePlayer == null) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Player is null")
        }

        val player = myPlayers.find { it.uuid == offlinePlayer.uniqueId } ?:
                return EconomyResponse(0.0, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Player is not exists")

        if (amount < 0) {
            return EconomyResponse(amount, player.balance,
                    EconomyResponse.ResponseType.FAILURE,
                    "Please enter a value of 0 or more")
        }

        player.balance += amount

        return EconomyResponse(amount, player.balance,
                EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun depositPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun depositPlayer(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): EconomyResponse {
        return depositPlayer(offlinePlayer, amount)
    }

    override fun createBank(bankName: String?, playerName: String?): EconomyResponse {
        return createBank(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun createBank(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        if (offlinePlayer == null) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Player is null")
        }

        if (myBanks.any { it.name == bankName }) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name <$bankName> is already exists")
        } else {
            val bank = MyBank(bankName, offlinePlayer.uniqueId)
            myBanks.add(bank)
            return EconomyResponse(0.0, bank.balance,
                    EconomyResponse.ResponseType.SUCCESS, null)
        }
    }

    override fun hasAccount(playerName: String?): Boolean {
        return hasAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun hasAccount(offlinePlayer: OfflinePlayer?): Boolean {
        if (offlinePlayer == null) return false
        return myPlayers.any { it.uuid == offlinePlayer.uniqueId }
    }

    override fun hasAccount(playerName: String?, worldName: String?): Boolean {
        return hasAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun hasAccount(offlinePlayer: OfflinePlayer?, worldName: String?): Boolean {
        return hasAccount(offlinePlayer)
    }

    override fun isBankMember(bankName: String?, playerName: String?): EconomyResponse {
        return isBankMember(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun isBankMember(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        if (offlinePlayer == null) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Player is null")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(0.0, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Bank is not exists")

        if (bank.members.contains(offlinePlayer.uniqueId)) {
            return EconomyResponse(0.0, bank.balance,
                    EconomyResponse.ResponseType.SUCCESS, null)
        } else {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Player is not bank member")
        }
    }

    override fun createPlayerAccount(playerName: String?): Boolean {
        return createPlayerAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun createPlayerAccount(offlinePlayer: OfflinePlayer?): Boolean {
        if (offlinePlayer == null) return false
        return myPlayers.add(MyPlayer(offlinePlayer.uniqueId))
    }

    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean {
        return createPlayerAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun createPlayerAccount(offlinePlayer: OfflinePlayer?, worldName: String?): Boolean {
        return createPlayerAccount(offlinePlayer)
    }

    override fun currencyNameSingular(): String = ""

    override fun withdrawPlayer(playerName: String?, amount: Double): EconomyResponse {
        return withdrawPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun withdrawPlayer(offlinePlayer: OfflinePlayer?, amount: Double): EconomyResponse {

        if (offlinePlayer == null) {
            return EconomyResponse(amount, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Player is null")
        }

        val player = myPlayers.find { it.uuid == offlinePlayer.uniqueId } ?:
                return EconomyResponse(amount, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "player is null")

        if (amount < 0) {
            return EconomyResponse(amount, player.balance,
                    EconomyResponse.ResponseType.FAILURE,
                    "Please enter a value of 0 or more")
        }

        if (player.balance - amount < 0) {
            return EconomyResponse(amount, player.balance,
                    EconomyResponse.ResponseType.FAILURE,
                    "Players do not have enough money")
        } else {
            player.balance -= amount
            return EconomyResponse(amount, player.balance,
                    EconomyResponse.ResponseType.SUCCESS, null)
        }
    }

    override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse {
        return withdrawPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun withdrawPlayer(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): EconomyResponse {
        return withdrawPlayer(offlinePlayer, amount)
    }

    override fun bankHas(bankName: String?, amount: Double): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(amount, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(amount, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Bank is not exists")

        if (amount < 0) {
            return EconomyResponse(amount, bank.balance,
                    EconomyResponse.ResponseType.FAILURE,
                    "Please enter a value of 0 or more")
        }

        if (bank.balance - amount < 0) {
            return EconomyResponse(amount, bank.balance,
                    EconomyResponse.ResponseType.FAILURE,
                    "The bank's deposit is insufficient")
        } else {
            return EconomyResponse(amount, bank.balance,
                    EconomyResponse.ResponseType.SUCCESS, null)
        }
    }

    override fun currencyNamePlural(): String = ""

    override fun isEnabled(): Boolean = true

    override fun fractionalDigits(): Int = -1

    override fun bankBalance(bankName: String?): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Bank name is null or blank")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(0.0, 0.0,
                        EconomyResponse.ResponseType.FAILURE,
                        "Bank is not exists")

        return EconomyResponse(0.0, bank.balance,
                EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun format(amount: Double): String = amount.toString()

    override fun hasBankSupport(): Boolean = false

    data class MyBank(
            var name: String,
            var owner: UUID,
            var balance: Double = 0.0,
            val members: MutableList<UUID> = mutableListOf()
    )

    data class MyPlayer(val uuid: UUID, var balance: Double = 0.0)
}
