package me.cerasi.listeners

import me.cerasi.MultiBot
import me.cerasi.events.Event
import me.cerasi.events.MessageEvent

open class Listener {
    companion object  {
        fun sendEvent(multiBot: MultiBot, event: Event) {
            try {
                multiBot.listeners.toList().forEach { listener ->
                    run {
                        listener.javaClass.declaredMethods.forEach {
                            if (it.isAnnotationPresent(EventHandler::class.java)
                                && it.parameterCount == 1
                                && it.parameterTypes[0].isAssignableFrom(event.javaClass)) {
                                it.invoke(listener, event)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                event.chat.sendMessage("Due to an internal error, I was unable to complete the requested operation. \n\nIf the problem persists, please contact the bot developer.")
                if (event is MessageEvent) event.accepted = true
                e.printStackTrace()
            }

            if (event !is MessageEvent) return

            if (event.chat.isGroup() || event.accepted) return
            event.chat.sendMessage("""                
                🕸️ *Unknown Operation*
                
                You have performed an operation that does not lead to any results.
                
                Type `/start` or `/help` to see the list of available commands, I will help you find what you need!
            """.trimIndent())
        }

    }

    annotation class EventHandler
}