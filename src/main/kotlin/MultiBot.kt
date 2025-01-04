package me.cerasi

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import it.auties.whatsapp.api.Whatsapp
import it.auties.whatsapp.model.info.ChatMessageInfo
import me.cerasi.command.Command
import me.cerasi.command.CommandListener
import me.cerasi.events.GroupJoinEvent
import me.cerasi.events.MessageEvent
import me.cerasi.listeners.Listener
import me.cerasi.listeners.ObserverManager
import me.cerasi.objects.Chat
import me.cerasi.objects.Message
import me.cerasi.objects.Service
import me.cerasi.objects.User
import java.util.*
import kotlin.jvm.optionals.getOrNull


class MultiBot {
    private val whatsappBots = mutableListOf<Whatsapp>()
    private val telegramBots = mutableListOf<TelegramBot>()

    val listeners = mutableListOf<Listener>()
    val commandList = mutableListOf<Command>()
    val observerManager = ObserverManager()


    init {
        registerListener(observerManager)
        registerListener(CommandListener(this))
    }

    fun registerCommand(command: Command) {
        commandList.add(command)
    }

    fun unRegisterCommand(command: Command) {
        commandList.remove(command)
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unRegisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun registerTelegramBot(bot: TelegramBot) {
        telegramBots.add(bot)

        bot.setUpdatesListener { updates ->
            updates.forEach { update ->
                try {
                    println(update.toString())

                    val multiChat = Chat(
                        service = Service.TELEGRAM,
                        id = update.message().chat().id().toString(),
                        name = if (update.message().chat().title() != null) update.message().chat()
                            .title() else update.message().chat().firstName(),
                        provider = bot,
                        origin = update.message().chat(),
                        this
                    )

                    if (update.message() != null) {
                        if (update.message().newChatMembers() != null && update.message().newChatMembers()
                                .isNotEmpty()
                        ) {
                            update.message().newChatMembers().forEach { user ->
                                val multiUser = User(
                                    id = user.id().toString(),
                                    name = user.username(),
                                    service = Service.TELEGRAM,
                                    provider = bot,
                                    origin = user,
                                    this
                                )

                                Listener.sendEvent(this, GroupJoinEvent(multiChat, multiUser))
                            }
                            return@forEach
                        }

                        val multiUser = User(
                            id = update.message().from().id().toString(),
                            name = update.message().from().firstName(),
                            service = Service.TELEGRAM,
                            provider = bot,
                            origin = update.message().from(),
                            this
                        )

                        val multiMessage = Message(
                            chat = multiChat, user = multiUser, text = if (update.message().text() != null) Optional.of(
                                update.message().text()
                            ) else Optional.empty(), origin = update.message(), this
                        )

                        Listener.sendEvent(this, MessageEvent(multiMessage))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            UpdatesListener.CONFIRMED_UPDATES_ALL

        }
    }

    fun registerWhatsappBot(bot: Whatsapp) {
        whatsappBots.add(bot)

        if (!bot.isConnected) Thread {
            bot.connect() // Connect to Whatsapp asynchronously
                .join() // Await the result
                .awaitDisconnection() // Wait
        }.start()

        bot.addNewChatMessageListener { provider, message ->
            val multiChat = Chat(
                service = Service.WHATSAPP,
                id = message.chatJid().toString(),
                name = message.chatName(),
                provider = provider,
                origin = message.chat().getOrNull(),
                this
            )

            println(message.toJson())

            val multiUser = User(
                id = message.senderJid().toString(),
                name = if (message.sender().isPresent) message.sender().get().name() else "unknown",
                service = Service.WHATSAPP,
                provider = provider,
                origin = message.sender().getOrNull(),
                this
            )

            val multiMessage = Message(
                chat = multiChat,
                user = multiUser,
                text = if(message.message().textMessage.isPresent) Optional.of(message.message().textMessage.get().text()) else message.message().textWithNoContextMessage,
                origin = message,
                this
            )

            if (!message.stubType().isPresent) {
                Listener.sendEvent(this, MessageEvent(multiMessage))
                return@addNewChatMessageListener
            }

            when (message.stubType().get()) {
                ChatMessageInfo.StubType.GROUP_CREATE -> Listener.sendEvent(this, GroupJoinEvent(multiChat, multiUser))
                else -> {}
            }
        }
    }
}