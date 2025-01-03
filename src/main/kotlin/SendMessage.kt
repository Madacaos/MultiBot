package me.cerasi

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import it.auties.whatsapp.api.Whatsapp
import it.auties.whatsapp.model.jid.Jid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.cerasi.objects.Chat
import me.cerasi.objects.Service

class SendMessage {
    lateinit var text: String
    lateinit var provider: Any
    var documents = mutableListOf<String>()

    fun send(chat: Chat) {
        if (chat.service == Service.TELEGRAM) {
            (provider as TelegramBot).execute(SendMessage(chat.id.toLong(), text).parseMode(ParseMode.Markdown))
            return
        }

        try {
            (provider as Whatsapp).sendMessage(Jid.of(chat.id), text)
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                text += """
                        📈 *Performance*
                        
                        This operation took longer than expected due to errors caused by Whatsapp. For better performance we invite you to use our Telegram version.
                    """.trimIndent()
                send(chat)
            }
        }
    }
}