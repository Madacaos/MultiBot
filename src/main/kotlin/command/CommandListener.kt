package me.cerasi.command

import me.cerasi.listeners.Listener
import me.cerasi.MultiBot
import me.cerasi.events.MessageEvent

class CommandListener(val multiBot: MultiBot) : Listener() {
    @EventHandler
    fun onMessage(messageEvent: MessageEvent) {
        if (!messageEvent.message.text.isPresent
            || !messageEvent.message.text.get().startsWith("/")) return

        multiBot.commandList.forEach {
            val commandInfo = it.getCommandInfo()
            val textMessage = messageEvent.message.text.get()

            val split = textMessage.split(" ")
            val alias = split[0].substring(1)
            val args = split.subList(1, split.size)

            if (alias.lowercase() != commandInfo.name.lowercase()) return@forEach
            if (args.size < commandInfo.minArgs || (commandInfo.maxArgs != -1 && args.size > commandInfo.maxArgs)) {
                messageEvent.message.chat.sendMessage("Use this syntax: ${commandInfo.usage}")
                return@forEach
            }

            it.onCommand(messageEvent.message, args)
            messageEvent.accepted = true
        }
    }
}