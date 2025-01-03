
# ⭐ MultiBot

A simple chatbot framework for WhatsApp and Telegram in Kotlin/Java, allowing multiple bots in a single codebase.

## Dependencies 
This project uses Pengrand for Telegram and Cobalt for WhatsApp to provide seamless interaction with both messaging platforms.

You can find the repositories here:

Pengrand: https://github.com/pengrad/java-telegram-bot-api

Cobalt: https://github.com/Auties00/Cobalt

## Implementation
```kts
dependencies {
    implementation("com.github.madacaos:multibot:-SNAPSHOT")
    implementation("com.github.auties00:cobalt:0.0.7") //Cobalt (whatsapp)
    implementation("com.github.pengrad:java-telegram-bot-api:7.9.1") //Pengrand (telegram)
}
```

## Initialization

```kotlin
fun main() {
    val whatsapp = Whatsapp.webBuilder() // Use the Web api
        .lastConnection() // Deserialize the last connection, or create a new one if it doesn't exist
        .unregistered(QrHandler.toTerminal()) // Print the QR to the terminal
        .addLoggedInListener { api: Whatsapp ->
            System.out.printf(
                "Connected",
            )
        }.addDisconnectedListener { reason: DisconnectReason? -> System.out.printf("Disconnected: %s%n", reason) }


    var telegram = TelegramBot("your-token")
    var multiBot = MultiBot()


    var multiBot = MultiBot()

    multiBot.registerTelegramBot(telegram)
    multiBot.registerWhatsappBot(whatsapp)
}
```

## Add a Listener

```kotlin
multiBot.registerListener(ExampleListener())

class ExampleListener : Listener() {
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
```

## Add a Command

```kotlin
multiBot.registerCommand(ExampleCommand())

@CommandInfo(
    name = "example",
    usage = "/example",
    minArgs = 0, maxArgs = 0
)
class ExampleCommand : Command() {
    override fun onCommand(message: Message, args: List<String>) {
        message.chat.sendMessage("This is an example command! Now, write something!")

        MessageObserver(message.chat) { messageEvent, messageObserver ->
            message.chat.sendMessage("You wrote: ${messageEvent.message.text}")
            messageObserver.unregister()
        }
    }
}
```