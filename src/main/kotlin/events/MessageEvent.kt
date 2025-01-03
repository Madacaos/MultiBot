package me.cerasi.events

import me.cerasi.objects.Message

class MessageEvent(val message: Message): Event(message.chat, message.user) {
    var accepted = false
}