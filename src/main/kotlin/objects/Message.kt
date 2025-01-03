package me.cerasi.objects

import me.cerasi.MultiBot
import java.util.*

class Message(val chat: Chat, val user: User, val text: Optional<String> = Optional.empty(), val origin: Any, multiBot: MultiBot) : MultiObject(multiBot) {
    fun isTextMessage(): Boolean = text.isPresent

    fun whatsappOrigin() : it.auties.whatsapp.model.message.model.Message = origin as it.auties.whatsapp.model.message.model.Message
    fun telegramOrigin() : com.pengrad.telegrambot.model.Message = origin as com.pengrad.telegrambot.model.Message

    fun isGroup() : Boolean {
        if (chat.service == Service.WHATSAPP) return chat.whatsappOrigin().isGroup()

        return false
    }
}