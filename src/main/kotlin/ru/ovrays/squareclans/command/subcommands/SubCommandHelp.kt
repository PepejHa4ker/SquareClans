package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.CommandClan
import ru.ovrays.squareclans.command.wrapper.CommandError
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ColorUtilities
import kotlin.math.ceil

object SubCommandHelp : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = false
    override val memberRelation: MemberRelation = MemberRelation.ANY
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("help")
    override val commandPermission: String = "SquareClans.command.help"
    override val description: String = "Помощь по плагину"

    private val header = ColorUtilities.GREEN+ "/c "

    public override fun execute(sender: WrappedSender, args: Array<String>) {

        var page = 1
        if(args.isNotEmpty()) {
            val arg = args[0]
            try {
                page = arg.toInt()
            } catch (e: Exception) {
                throw CommandError("Неверный номер: $arg", false)
            }
        }

        val entries = 6
        val count = CommandClan.subcommands.size
        val pages = ceil(count.toFloat() / entries.toFloat()).toInt()

        page = page.coerceIn(1..pages)

        sender.sendMessage(ColorUtilities.GREEN + "&8&m------------[-&r &aПомощь &8[&a$page&8/&a$pages&8] &8&m-]------------")
        val index = entries * (page - 1)
        val ar = CommandClan.subcommands.subList(index, (index + entries).coerceAtMost(count))

        ar.forEach {
            if(sender.hasPermission(it.commandPermission)) {
                sender.sendMessage(it.helpEntry)
            }
        }


        sender.sendMessage(ColorUtilities.GREEN + "&8&m------------[-&r &8&m-----------&r &8&m-]------------")
    }

    override val usage = createUsage("help")
}