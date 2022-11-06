package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand.Companion.homeCommand
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.defaultLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.cancelTeleport
import com.masahirosaito.spigot.homes.testutils.getDelayThread
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DelayTeleporterTest {

    @BeforeEach
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @AfterEach
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun run() {
        DelayTeleporter.run(nepian, defaultLocation, homeCommand)
        nepian.getDelayThread()?.let {
            assertTrue(nepian.hasMetadata("homes.delay"))
            assertTrue(it.isAlive)
            it.join()
        }
        nepian.cancelTeleport()
    }

    @Test
    fun isAlreadyRun() {
        DelayTeleporter.run(nepian, defaultLocation, homeCommand)
        assertTrue(DelayTeleporter.isAlreadyRun(nepian))
        nepian.cancelTeleport()
    }

    @Test
    fun cancelTeleport() {
        DelayTeleporter.run(nepian, defaultLocation, homeCommand)
        nepian.getDelayThread()?.let {
            DelayTeleporter.cancelTeleport(nepian)
            it.join()
            assertFalse(it.isAlive)
            assertFalse(nepian.hasMetadata("homes.delay"))
        }
    }

}
