package ru.ovrays.squareclans.command.wrapper

import ru.ovrays.squareclans.util.Executor

class CommandBuilder {

    private var permission: String? = null
    private var description: String? = null
    private var children: Map<List<String>, CommandSpec>? = null
    private var executor: Executor? = null
    private var usage: String? = null

    fun permission(str: String): CommandBuilder = this.apply {
        permission = str
    }

    fun description(str: String): CommandBuilder = this.apply {
        description = str
    }

    fun children(cmdMap: HashMap<List<String>, CommandSpec>): CommandBuilder = this.apply {
        children = cmdMap
    }

    fun executor(e: Executor): CommandBuilder = this.apply {
        executor = e
    }

    fun usage(u: String): CommandBuilder = this.apply {
        usage = u
    }

    fun build(): CommandSpec {
        return CommandSpec(
            permission, description, children, executor!!, usage
        )
    }

}
