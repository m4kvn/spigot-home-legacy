package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.exceptions.AlreadyExecutTeleportException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindOnlinePlayerException
import com.masahirosaito.spigot.homes.strings.TeleportStrings
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.concurrent.thread

object DelayTeleporter {
    const val DELAY_META: String = "homes.delay"

    fun run(player: Player, location: Location) {
        if (Configs.teleportDelay <= 0) {
            player.teleport(location)
            return
        }

        val delay = Configs.teleportDelay
        val uuid = player.uniqueId

        if (isAlreadyRun(player)) {
            throw AlreadyExecutTeleportException()
        }

        val th = thread {
            try {
                if (delay > 0) {
                    Messenger.send(player, TeleportStrings.TELEPORT_WAIT(delay))
                    Thread.sleep(delay * 1000L)
                }
                findOnlinePlayer(uuid).run {
                    teleport(location)
                    removeTeleportMeta(player)
                }
            } catch (e: InterruptedException) {
                removeTeleportMeta(player)
                Messenger.send(player, TeleportStrings.TELEPORT_CANCEL())
                Messenger.send(player, TeleportStrings.TELEPORT_CANCEL_DELAY(delay))
            } catch (e: CanNotFindOnlinePlayerException) {

            }
        }

        player.setMetadata(DELAY_META, FixedMetadataValue(Homes.homes, th))
    }

    fun isAlreadyRun(player: Player): Boolean {
        return player.hasMetadata(DELAY_META)
    }

    private fun removeTeleportMeta(player: Player) {
        if (isAlreadyRun(player)) {
            player.removeMetadata(DELAY_META, Homes.homes)
        }
    }

    fun cancelTeleport(player: Player) {
        (player.getMetadata(DELAY_META).first().value() as Thread).run {
            if (isAlive) {
                interrupt()
            }
        }
        removeTeleportMeta(player)
    }
}
