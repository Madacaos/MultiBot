package me.cerasi.command

import me.cerasi.objects.Message

abstract class Command() {
    fun getCommandInfo() : CommandInfo {
        return this::class.java.getAnnotation(CommandInfo::class.java)
    }

    abstract fun onCommand(message: Message, args: List<String>)
}