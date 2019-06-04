package ru.ovrays.squareclans.command.wrapper

class CommandError(override val message: String?, val showUsage: Boolean) : RuntimeException()