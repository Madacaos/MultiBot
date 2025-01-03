package me.cerasi.events

import me.cerasi.objects.Chat
import me.cerasi.objects.User

class GroupJoinEvent(chat: Chat, user: User): Event(chat, user) {

}