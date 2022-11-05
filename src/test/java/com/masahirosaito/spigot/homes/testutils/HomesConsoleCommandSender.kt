package com.masahirosaito.spigot.homes.testutils

import org.bukkit.Server
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.conversations.Conversation
import org.bukkit.conversations.ConversationAbandonedEvent
import org.bukkit.permissions.Permissible
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.plugin.Plugin
import org.mockito.Mockito.mock

class HomesConsoleCommandSender(
    private val mockServer: Server,
) : ConsoleCommandSender {

    override fun sendMessage(message: String?) {
        mockServer.logger.info(message)
    }

    override fun sendMessage(messages: Array<out String>?) {
        messages?.forEach { mockServer.logger.info(it) }
    }

    override fun beginConversation(conversation: Conversation?): Boolean = true

    override fun isPermissionSet(name: String?): Boolean = true

    override fun isPermissionSet(perm: Permission?): Boolean = true

    override fun addAttachment(plugin: Plugin?, name: String?, value: Boolean): PermissionAttachment {
        return PermissionAttachment(plugin, mock(Permissible::class.java))
    }

    override fun addAttachment(plugin: Plugin?): PermissionAttachment {
        return PermissionAttachment(plugin, mock(Permissible::class.java))
    }

    override fun addAttachment(plugin: Plugin?, name: String?, value: Boolean, ticks: Int): PermissionAttachment {
        return PermissionAttachment(plugin, mock(Permissible::class.java))
    }

    override fun addAttachment(plugin: Plugin?, ticks: Int): PermissionAttachment {
        return PermissionAttachment(plugin, mock(Permissible::class.java))
    }

    override fun getName(): String = "HomesConsoleSender"

    override fun isOp(): Boolean = true

    override fun acceptConversationInput(input: String?) { }

    override fun sendRawMessage(message: String?) {
        mockServer.logger.info(message)
    }

    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> = mutableSetOf()

    override fun isConversing(): Boolean = true

    override fun getServer(): Server = mockServer

    override fun removeAttachment(attachment: PermissionAttachment?) { }

    override fun recalculatePermissions() { }

    override fun hasPermission(name: String?): Boolean = true

    override fun hasPermission(perm: Permission?): Boolean = true

    override fun abandonConversation(conversation: Conversation?) { }

    override fun abandonConversation(conversation: Conversation?, details: ConversationAbandonedEvent?) { }

    override fun setOp(value: Boolean) { }
}
