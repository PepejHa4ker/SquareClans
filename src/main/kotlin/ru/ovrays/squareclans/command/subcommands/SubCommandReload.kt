package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ColorUtilities


object SubCommandReload : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = false
    override val memberRelation: MemberRelation = MemberRelation.ANY
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("reload")
    override val commandPermission: String = "SquareClans.command.reload"
    override val description: String = "Перезагрузить плагин"
    override val usage: String = createUsage("reload")
    override fun execute(sender: WrappedSender, args: Array<String>) {
        DataLoader.load()
        sender.sendMessage((ColorUtilities.GREEN+"Плагин перезагружен"))
    }
}