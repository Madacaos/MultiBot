package me.cerasi.command

annotation class CommandInfo(
    val name: String,
    val description: String = "", // Not use, why not remove?
    val usage: String,
    val minArgs: Int = 0,
    val maxArgs: Int = 0,
)
