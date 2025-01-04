package me.cerasi.objects

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.GetChat
import it.auties.whatsapp.api.Whatsapp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.cerasi.MultiBot
import me.cerasi.SendMessage
import java.util.concurrent.CompletableFuture

class Chat(var service: Service, var id: String, var name: String, val provider: Any, val origin: Any?, multiBot: MultiBot) : MultiObject(multiBot) {
    fun whatsappOrigin() : it.auties.whatsapp.model.chat.Chat = origin as it.auties.whatsapp.model.chat.Chat
    fun telegramOrigin() : com.pengrad.telegrambot.model.Chat = origin as com.pengrad.telegrambot.model.Chat

    fun isGroup() : Boolean {
        if (service == Service.WHATSAPP) return whatsappOrigin().isGroup
        if (service == Service.TELEGRAM) return telegramOrigin().type() == com.pengrad.telegrambot.model.Chat.Type.group

        return false
    }

    fun sendMessage(text: String, documents: MutableList<String> = mutableListOf()): CompletableFuture<SendMessage> {
        val last = delayMessage.getOrPut(id) { System.currentTimeMillis() }
        delayMessage[id] = (if(System.currentTimeMillis() > last) System.currentTimeMillis() else last) + DELAY_TIME

        val future = CompletableFuture<SendMessage>()

        val sendMessage = SendMessage()
        sendMessage.text = text
        sendMessage.provider = provider
        sendMessage.documents = documents


        CoroutineScope(Dispatchers.IO).launch {
            val delayTime = last - System.currentTimeMillis()

            if (delayTime > 0) {
                delay(delayTime)
            }

            sendMessage.send(this@Chat)
            future.complete(sendMessage)
        }

        return future
    }

    companion object {
        val delayMessage = mutableMapOf<String, Long>()
        const val DELAY_TIME: Long = 2 * 1000

        fun getChat(id: String, multiBot: MultiBot, provider: Any): Chat? {
            if (provider is TelegramBot)
                return Chat(Service.TELEGRAM, id, "", provider, provider.execute(GetChat(id)), multiBot)
            else if (provider is Whatsapp)
                return Chat(Service.WHATSAPP, id, "", provider, null, multiBot)

            return null
        }
    }
}