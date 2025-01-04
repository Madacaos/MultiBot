package me.cerasi

import com.pengrad.telegrambot.TelegramBot
import it.auties.whatsapp.api.DisconnectReason
import it.auties.whatsapp.api.QrHandler
import it.auties.whatsapp.api.Whatsapp

fun main() {
    val whatsapp = Whatsapp.webBuilder() // Use the Web api
        .lastConnection() // Deserialize the last connection, or create a new one if it doesn't exist
        .unregistered(QrHandler.toTerminal()) // Print the QR to the terminal
        .addLoggedInListener { _: Whatsapp ->
            System.out.printf(
                "Connected",
            )
        }.addDisconnectedListener { reason: DisconnectReason? -> System.out.printf("Disconnected: %s%n", reason) }

    val telegram = TelegramBot("your-token")

    val multiBot = MultiBot()

    multiBot.registerTelegramBot(telegram)
    multiBot.registerWhatsappBot(whatsapp)

    //Can include more bots
    //multiBot.registerWhatsappBot(whatsapp2)
    //multiBot.registerTelegramBot(telegram2)

    multiBot.registerListener(ExampleListener())
    multiBot.registerCommand(ExampleCommand())
}