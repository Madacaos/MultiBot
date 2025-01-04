package me.cerasi.listeners

import me.cerasi.events.MessageEvent
import me.cerasi.objects.Chat
import me.cerasi.objects.User
import kotlin.jvm.optionals.getOrDefault

class MessageObserver(
    val chat: Chat,
    val user: User? = null,
    private val nullable: Boolean = true,
    private val action: (MessageEvent, MessageObserver) -> Unit
) {
    init {
        register()
    }

    private fun register() {
        chat.multiBot.observerManager.register(this)
    }

    fun unregister() {
        chat.multiBot.observerManager.unregister(this)
    }

    fun execute(event: MessageEvent) {
        if (nullable && event.message.text.getOrDefault("null").lowercase().startsWith("annull")) {
            unregister()
            event.chat.sendMessage("""
                Action Canceled ❌

                You canceled the action you were performing, if you don't know how to continue send the command `/start` or `/help`.
            """.trimIndent())
            return
        }

        action(event, this)
    }
}
