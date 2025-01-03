package me.cerasi

import me.cerasi.command.Command
import me.cerasi.command.CommandInfo
import me.cerasi.listeners.MessageObserver
import me.cerasi.objects.Message

@CommandInfo(
    name = "example",
    usage = "/example",
    minArgs = 0, maxArgs = 0
)
class ExampleCommand: Command() {
    override fun onCommand(message: Message, args: List<String>) {
        message.chat.sendMessage("This is an example command! Now, write something!")

        MessageObserver(message.chat) { messageEvent, messageObserver ->
            message.chat.sendMessage("You wrote: ${messageEvent.message.text}")
            messageObserver.unregister()
        }
    }
}