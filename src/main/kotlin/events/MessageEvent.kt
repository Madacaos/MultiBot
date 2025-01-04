package me.cerasi.events

import me.cerasi.objects.Message

class MessageEvent(val message: Message, var accepted: Boolean = false): Event(message.chat, message.user)