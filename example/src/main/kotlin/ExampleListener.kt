package me.cerasi

import me.cerasi.events.MessageEvent
import me.cerasi.listeners.Listener

class ExampleListener: Listener() {
    @EventHandler
    fun onMessage(event: MessageEvent) {
        event.message.chat.sendMessage(
            """
                Hello, ${event.message.user.name}!
                You sent: ${event.message.text}
            """.trimIndent()
        )
    }
}