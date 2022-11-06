package com.masahirosaito.spigot.homes.testutils

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*

class MyEconomy : Economy {
    private val myBanks: MutableList<MyBank> = mutableListOf()
    private val myPlayers: MutableList<MyPlayer> = mutableListOf()

    override fun getBanks(): MutableList<String> {
        return myBanks.map { it.name }.toMutableList()
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "getBalance(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun getBalance(playerName: String?): Double {
        return getBalance(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun getBalance(offlinePlayer: OfflinePlayer?): Double {
        return myPlayers.find { it.uuid == offlinePlayer?.uniqueId }!!.balance
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "getBalance(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun getBalance(playerName: String?, worldName: String?): Double {
        return getBalance(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun getBalance(offlinePlayer: OfflinePlayer?, worldName: String?): Double {
        return getBalance(offlinePlayer)
    }

    override fun getName(): String = "Homes Economy"

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "isBankOwner(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun isBankOwner(bankName: String?, playerName: String?): EconomyResponse {
        return isBankOwner(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun isBankOwner(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        if (offlinePlayer == null) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Player is null"
            )
        }

        val bank = myBanks.find { it.name == bankName } ?: return EconomyResponse(
            0.0, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Bank is null"
        )

        return if (bank.owner == offlinePlayer.uniqueId) {
            EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "You are not bank owner"
            )
        } else {
            EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.SUCCESS, null
            )
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "has(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)",
            "org.bukkit.Bukkit"
        )
    )
    override fun has(playerName: String?, amount: Double): Boolean {
        return has(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun has(offlinePlayer: OfflinePlayer?, amount: Double): Boolean {
        return getBalance(offlinePlayer) - amount >= 0
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "has(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)",
            "org.bukkit.Bukkit"
        )
    )
    override fun has(playerName: String?, worldName: String?, amount: Double): Boolean {
        return has(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun has(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): Boolean {
        return has(offlinePlayer, amount)
    }

    override fun bankDeposit(bankName: String?, amount: Double): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        val bank = myBanks.find { it.name == bankName } ?: return EconomyResponse(
            0.0, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Bank is null"
        )

        if (amount < 0) {
            return EconomyResponse(
                amount, bank.balance,
                EconomyResponse.ResponseType.FAILURE,
                "Please enter a value of 0 or more"
            )
        }

        bank.balance += amount

        return EconomyResponse(
            amount, bank.balance,
            EconomyResponse.ResponseType.SUCCESS, null
        )
    }

    override fun bankWithdraw(bankName: String?, amount: Double): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        val bank = myBanks.find { it.name == bankName } ?: return EconomyResponse(
            0.0, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Bank is null"
        )

        if (amount < 0) {
            return EconomyResponse(
                amount, bank.balance,
                EconomyResponse.ResponseType.FAILURE,
                "Please enter a value of 0 or more"
            )
        }

        bank.balance -= amount

        return EconomyResponse(
            amount, bank.balance,
            EconomyResponse.ResponseType.SUCCESS, null
        )
    }

    override fun deleteBank(bankName: String?): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        val bank = myBanks.find { it.name == bankName } ?: return EconomyResponse(
            0.0, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Bank is null"
        )

        myBanks.remove(bank)

        return EconomyResponse(
            0.0, bank.balance,
            EconomyResponse.ResponseType.SUCCESS, null
        )
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "depositPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)",
            "org.bukkit.Bukkit"
        )
    )
    override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun depositPlayer(offlinePlayer: OfflinePlayer?, amount: Double): EconomyResponse {

        if (offlinePlayer == null) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Player is null"
            )
        }

        val player = myPlayers.find { it.uuid == offlinePlayer.uniqueId } ?: return EconomyResponse(
            0.0, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Player is not exists"
        )

        if (amount < 0) {
            return EconomyResponse(
                amount, player.balance,
                EconomyResponse.ResponseType.FAILURE,
                "Please enter a value of 0 or more"
            )
        }

        player.balance += amount

        return EconomyResponse(
            amount, player.balance,
            EconomyResponse.ResponseType.SUCCESS, null
        )
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "depositPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)",
            "org.bukkit.Bukkit"
        )
    )
    override fun depositPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun depositPlayer(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): EconomyResponse {
        return depositPlayer(offlinePlayer, amount)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "createBank(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun createBank(bankName: String?, playerName: String?): EconomyResponse {
        return createBank(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun createBank(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        if (offlinePlayer == null) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Player is null"
            )
        }

        return if (myBanks.any { it.name == bankName }) {
            EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name <$bankName> is already exists"
            )
        } else {
            val bank = MyBank(bankName, offlinePlayer.uniqueId)
            myBanks.add(bank)
            EconomyResponse(
                0.0, bank.balance,
                EconomyResponse.ResponseType.SUCCESS, null
            )
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "hasAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun hasAccount(playerName: String?): Boolean {
        return hasAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun hasAccount(offlinePlayer: OfflinePlayer?): Boolean {
        if (offlinePlayer == null) return false
        return myPlayers.any { it.uuid == offlinePlayer.uniqueId }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "hasAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun hasAccount(playerName: String?, worldName: String?): Boolean {
        return hasAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun hasAccount(offlinePlayer: OfflinePlayer?, worldName: String?): Boolean {
        return hasAccount(offlinePlayer)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "isBankMember(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun isBankMember(bankName: String?, playerName: String?): EconomyResponse {
        return isBankMember(bankName, Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun isBankMember(bankName: String?, offlinePlayer: OfflinePlayer?): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        if (offlinePlayer == null) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Player is null"
            )
        }

        val bank = myBanks.find { it.name == bankName } ?: return EconomyResponse(
            0.0, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Bank is not exists"
        )

        return if (bank.members.contains(offlinePlayer.uniqueId)) {
            EconomyResponse(
                0.0, bank.balance,
                EconomyResponse.ResponseType.SUCCESS, null
            )
        } else {
            EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Player is not bank member"
            )
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "createPlayerAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun createPlayerAccount(playerName: String?): Boolean {
        return createPlayerAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun createPlayerAccount(offlinePlayer: OfflinePlayer?): Boolean {
        if (offlinePlayer == null) return false
        return myPlayers.add(MyPlayer(offlinePlayer.uniqueId))
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "createPlayerAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })",
            "org.bukkit.Bukkit"
        )
    )
    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean {
        return createPlayerAccount(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName })
    }

    override fun createPlayerAccount(offlinePlayer: OfflinePlayer?, worldName: String?): Boolean {
        return createPlayerAccount(offlinePlayer)
    }

    override fun currencyNameSingular(): String = ""

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "withdrawPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)",
            "org.bukkit.Bukkit"
        )
    )
    override fun withdrawPlayer(playerName: String?, amount: Double): EconomyResponse {
        return withdrawPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun withdrawPlayer(offlinePlayer: OfflinePlayer?, amount: Double): EconomyResponse {

        if (offlinePlayer == null) {
            return EconomyResponse(
                amount, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Player is null"
            )
        }

        val player = myPlayers.find { it.uuid == offlinePlayer.uniqueId } ?: return EconomyResponse(
            amount, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "player is null"
        )

        if (amount < 0) {
            return EconomyResponse(
                amount, player.balance,
                EconomyResponse.ResponseType.FAILURE,
                "Please enter a value of 0 or more"
            )
        }

        return if (player.balance - amount < 0) {
            EconomyResponse(
                amount, player.balance,
                EconomyResponse.ResponseType.FAILURE,
                "Players do not have enough money"
            )
        } else {
            player.balance -= amount
            EconomyResponse(
                amount, player.balance,
                EconomyResponse.ResponseType.SUCCESS, null
            )
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "withdrawPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)",
            "org.bukkit.Bukkit"
        )
    )
    override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse {
        return withdrawPlayer(Bukkit.getOfflinePlayers().firstOrNull { it.name == playerName }, amount)
    }

    override fun withdrawPlayer(offlinePlayer: OfflinePlayer?, worldName: String?, amount: Double): EconomyResponse {
        return withdrawPlayer(offlinePlayer, amount)
    }

    override fun bankHas(bankName: String?, amount: Double): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                amount, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        val bank = myBanks.find { it.name == bankName } ?: return EconomyResponse(
            amount, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Bank is not exists"
        )

        if (amount < 0) {
            return EconomyResponse(
                amount, bank.balance,
                EconomyResponse.ResponseType.FAILURE,
                "Please enter a value of 0 or more"
            )
        }

        return if (bank.balance - amount < 0) {
            EconomyResponse(
                amount, bank.balance,
                EconomyResponse.ResponseType.FAILURE,
                "The bank's deposit is insufficient"
            )
        } else {
            EconomyResponse(
                amount, bank.balance,
                EconomyResponse.ResponseType.SUCCESS, null
            )
        }
    }

    override fun currencyNamePlural(): String = ""

    override fun isEnabled(): Boolean = true

    override fun fractionalDigits(): Int = -1

    override fun bankBalance(bankName: String?): EconomyResponse {

        if (bankName.isNullOrBlank()) {
            return EconomyResponse(
                0.0, 0.0,
                EconomyResponse.ResponseType.FAILURE,
                "Bank name is null or blank"
            )
        }

        val bank = myBanks.find { it.name == bankName } ?: return EconomyResponse(
            0.0, 0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Bank is not exists"
        )

        return EconomyResponse(
            0.0, bank.balance,
            EconomyResponse.ResponseType.SUCCESS, null
        )
    }

    override fun format(amount: Double): String = amount.toString()

    override fun hasBankSupport(): Boolean = false

    fun clear() {
        myBanks.clear()
        myPlayers.clear()
    }

    data class MyBank(
        var name: String,
        var owner: UUID,
        var balance: Double = 0.0,
        val members: MutableList<UUID> = mutableListOf()
    )

    data class MyPlayer(val uuid: UUID, var balance: Double = 0.0)
}
