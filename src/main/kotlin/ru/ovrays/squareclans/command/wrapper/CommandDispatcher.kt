package ru.ovrays.squareclans.command.wrapper

import org.bukkit.command.CommandSender

object CommandDispatcher {

    private val allAliases = hashSetOf<String>()
    private val commands = hashMapOf<String, CommandSpec>()

    fun register(obj: CommandSpec, vararg aliases: String) {
        allAliases += aliases
        aliases.forEach {
            commands[it] = obj
        }
    }

    fun onCommand(
        sender: CommandSender,
        label: String,
        args: Array<String>
    ): Boolean {
        if(label.toLowerCase() in allAliases) {
            val obj = commands[label.toLowerCase()]!!
            val wrap = WrappedSender(sender)

            obj.execute(wrap,args)

            return true
        }
        return false
    }
}