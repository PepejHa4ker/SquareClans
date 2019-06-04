package ru.ovrays.squareclans.command.wrapper

import ru.ovrays.squareclans.util.Executor


class   CommandSpec
    (
    private var permission: String? = null,
    private var description: String? = null,
    private var children: Map<List<String>, CommandSpec>? = null,
    var executor: Executor,
    var usage: String?
) {

    companion object {
        fun builder() = CommandBuilder()

        private fun call(commandSpec: CommandSpec, src: WrappedSender, args: Array<String>) {

            if (commandSpec.permission != null && !src.hasPermission(commandSpec.permission!!)) {
                src.error("Недостаточно прав")
                return
            }

            try {
                commandSpec.executor(src, args)
            } catch (e: CommandError) {
                var er = "[Ошибка] ${e.message}."
                if (e.showUsage && commandSpec.usage != null) {
                    er += "\nИспользование: " + commandSpec.usage!!
                }
                src.error(er)
            }
        }
    }


    fun execute(src: WrappedSender, args: Array<String>) {

        val hasChildren = children?.isNotEmpty() == true
        if (!hasChildren) {
            call(this, src, args)
        } else {
            if (args.isEmpty()) {
                call(this, src, args)
            } else {
                val name = args[0].toLowerCase()
                val children = children!!.filter {
                    it.key.contains(name)
                }.values
                if (children.isEmpty()) {
                    call(this, src, args)
                } else {
                    val child = children.first()
                    call(child, src, args.toList().drop(1).toTypedArray())
                }
            }
        }
    }
}
