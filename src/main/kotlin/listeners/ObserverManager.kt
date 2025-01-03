package me.cerasi.listeners

import me.cerasi.events.MessageEvent

class ObserverManager: Listener() {
    private val observers = mutableSetOf<MessageObserver>()

    fun register(observer: MessageObserver) {
        observers.add(observer)
    }

    fun unregister(observer: MessageObserver) {
        observers.remove(observer)
    }

    @EventHandler
    fun onEvent(event: MessageEvent) {
        val chat = event.chat
        val user = event.user

        observers.forEach { observer ->
            if (observer.user != null && user.id != observer.user.id) return@forEach
            if (chat.id != observer.chat.id) return@forEach

            event.accepted = true
            observer.execute(event)
        }
    }
}