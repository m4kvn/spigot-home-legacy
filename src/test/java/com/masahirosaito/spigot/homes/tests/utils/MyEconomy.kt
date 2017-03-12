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
        return getBalance(Bukkit.getOfflinePlayer(playerName))
    }

    override fun getBalance(offlinePlayer: OfflinePlayer?): Double {
        return myPlayers.find { it.uuid == offlinePlayer?.uniqueId }!!.balance
    }

    override fun getBalance(playerName: String?, worldName: String?): Double {
        return getBalance(playerName)
    }

    override fun getBalance(offlinePlayer: OfflinePlayer?, worldName: String?): Double {
        return getBalance(offlinePlayer)
    }

    override fun getName(): String = "Homes Economy"

    override fun isBankOwner(bankName: String?, playerName: String?): EconomyResponse {
        return isBankOwner(bankName, Bukkit.getOfflinePlayer(playerName))
    }

    override fun isBankOwner(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {
        val bank = myBanks.find { it.name == bankName }
        var res = EconomyResponse.ResponseType.SUCCESS
        var error: String? = null

        when {
            bank == null -> {
                res = EconomyResponse.ResponseType.FAILURE
                error = "The bank is not exists"
            }
            bank.owner != offlinePlayer?.uniqueId -> {
                res = EconomyResponse.ResponseType.FAILURE
                error = "You are not bank owner"
            }
        }

        return EconomyResponse(0.0, bank?.balance ?: 0.0, res, error)
    }

    override fun has(playerName: String?, amount: Double): Boolean {
        return has(Bukkit.getOfflinePlayer(playerName), amount)
    }

    override fun has(offlinePlayer: OfflinePlayer?, amount: Double): Boolean {
        return getBalance(offlinePlayer) - amount >= 0
    }

    override fun has(playerName: String?, worldName: String?, amount: Double): Boolean {
        return has(playerName, amount)
    }

    override fun has(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): Boolean {
        return has(offlinePlayer, amount)
    }

    override fun bankDeposit(bankName: String?, amount: Double): EconomyResponse {
        val bank = myBanks.find { it.name == bankName }
        var res = EconomyResponse.ResponseType.SUCCESS
        var error: String? = null

        if (bank == null) {
            res = EconomyResponse.ResponseType.FAILURE
            error = "The bank is not exists"
        } else {
            if (amount < 0) {
                res = EconomyResponse.ResponseType.FAILURE
                error = "Please enter a value of 0 or more"
            } else {
                bank.balance += amount
            }
        }

        return EconomyResponse(amount, bank?.balance ?: 0.0, res, error)
    }

    override fun bankWithdraw(bankName: String?, amount: Double): EconomyResponse {
        val bank = myBanks.find { it.name == bankName }
        var res = EconomyResponse.ResponseType.SUCCESS
        var error: String? = null

        if (bank == null) {
            res = EconomyResponse.ResponseType.FAILURE
            error = "The bank is not exists"
        } else {
            if (amount < 0) {
                res = EconomyResponse.ResponseType.FAILURE
                error = "Please enter a value of 0 or more"
            } else if (bank.balance - amount < 0) {
                res = EconomyResponse.ResponseType.FAILURE
                error = "The bank's deposit is insufficient"
            } else {
                bank.balance -= amount
            }
        }

        return EconomyResponse(amount, bank?.balance ?: 0.0, res, error)
    }

    override fun deleteBank(bankName: String?): EconomyResponse {
        val bank = myBanks.find { it.name == bankName }
        var res = EconomyResponse.ResponseType.SUCCESS
        var error: String? = null

        if (bank != null) {
            if (!myBanks.remove(bank)) {
                res = EconomyResponse.ResponseType.FAILURE
                error = "Can not remove the bank"
            }
        } else {
            res = EconomyResponse.ResponseType.FAILURE
            error = "The bank is not exists"
        }

        return EconomyResponse(0.0, bank?.balance ?: 0.0, res, error)
    }

    override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount)
    }

    override fun depositPlayer(offlinePlayer: OfflinePlayer?, amount: Double): EconomyResponse {
        val player = myPlayers.find { it.uuid == offlinePlayer?.uniqueId }
        var res = EconomyResponse.ResponseType.SUCCESS
        var error: String? = null

        when {
            player == null -> {
                res = EconomyResponse.ResponseType.FAILURE
                error = "Player is not exists"
            }
            amount < 0 -> {
                res = EconomyResponse.ResponseType.FAILURE
                error = "Please enter a value of 0 or more"
            }
            else -> {
                player.balance += amount
            }
        }

        return EconomyResponse(amount, player?.balance ?: 0.0, res, error)
    }

    override fun depositPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse {
        return depositPlayer(playerName, amount)
    }

    override fun depositPlayer(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): EconomyResponse {
        return depositPlayer(offlinePlayer, amount)
    }

    override fun createBank(bankName: String?, playerName: String?): EconomyResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createBank(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {
        val error: String= when {
            bankName == null || bankName.isBlank() -> "Bank name is null or blank"
            offlinePlayer == null                  -> "Player is null"
            myBanks.any { it.name == bankName }    -> "Bank name <$bankName> is already exists"
            else -> {
                val bank = MyBank(bankName, offlinePlayer.uniqueId)
                val res = EconomyResponse.ResponseType.SUCCESS
                return EconomyResponse(0.0, bank.balance, res, null)
            }
        }

        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, error)
    }

    override fun hasAccount(playerName: String?): Boolean {
        return hasAccount(Bukkit.getOfflinePlayer(playerName))
    }

    override fun hasAccount(offlinePlayer: OfflinePlayer?): Boolean {
        return if (offlinePlayer == null) false else myPlayers.any { it.uuid == offlinePlayer.uniqueId }
    }

    override fun hasAccount(playerName: String?, worldName: String?): Boolean {
        return hasAccount(playerName)
    }

    override fun hasAccount(offlinePlayer: OfflinePlayer?, worldName: String?): Boolean {
        return hasAccount(offlinePlayer)
    }

    override fun isBankMember(bankName: String?, playerName: String?): EconomyResponse {
        return isBankMember(bankName, Bukkit.getOfflinePlayer(playerName))
    }

    override fun isBankMember(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {

        if (bankName == null || bankName.isBlank()) {
            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Bank name is null or blank")
        }

        if (offlinePlayer == null) {
            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player is null")
        }

        val bank = myBanks.find { it.name == bankName } ?:
                return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Bank is not exists")

        if (bank.members.contains(offlinePlayer.uniqueId)) {
            return EconomyResponse(0.0, bank.balance, EconomyResponse.ResponseType.SUCCESS, null)
        } else {
            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player is not bank member")
        }
    }

    override fun createPlayerAccount(p0: String?): Boolean {
        val op = Bukkit.getOfflinePlayer(p0) ?: return false
        return true.apply { players.put(op, 0.0) }
    }

    override fun createPlayerAccount(p0: OfflinePlayer?): Boolean {
        return true.apply { players.put(p0!!, 0.0) }
    }

    override fun createPlayerAccount(p0: String?, p1: String?): Boolean {
        return createPlayerAccount(p0)
    }

    override fun createPlayerAccount(p0: OfflinePlayer?, p1: String?): Boolean {
        return createPlayerAccount(p0)
    }

    override fun currencyNameSingular(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun withdrawPlayer(p0: String?, p1: Double): EconomyResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun withdrawPlayer(p0: OfflinePlayer?, p1: Double): EconomyResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun withdrawPlayer(p0: String?, p1: String?, p2: Double): EconomyResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun withdrawPlayer(p0: OfflinePlayer?, p1: String?, p2: Double): EconomyResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bankHas(p0: String?, p1: Double): EconomyResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun currencyNamePlural(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEnabled(): Boolean = true

    override fun fractionalDigits(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bankBalance(bankName: String?): EconomyResponse {
        val bank = myBanks.find { it.name == bankName }
        return if (bank != null) {
            EconomyResponse(0.0, bank.amount, EconomyResponse.ResponseType.SUCCESS, null)
        } else {
            EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Bank is not exists")
        }
    }

    override fun format(p0: Double): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasBankSupport(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    data class MyBank(
            var name: String,
            var owner: UUID,
            var balance: Double = 0.0,
            val members: MutableList<UUID> = mutableListOf()
    )

    data class MyPlayer(val uuid: UUID, var balance: Double)
}
