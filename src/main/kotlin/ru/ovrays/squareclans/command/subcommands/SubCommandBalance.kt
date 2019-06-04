package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ClanPlayer
import ru.ovrays.squareclans.util.ColorUtilities

object SubCommandBalance : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = false
    override val memberRelation: MemberRelation = MemberRelation.MEMBER
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("balance")
    override val commandPermission: String = "SquareClans.command.balance"
    override val description: String = "Узнать свой вклад в клан"
    override val usage: String = createUsage("balance")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if(args.isNotEmpty()) {
            invalidArgsCountError(0, args.size)
        }

        val cp = sender.sourcePlayer.getMember()

        sender.sendMessage("&aВаш баланс: &c${SubCommandPoints.getSuffixed(cp.points)}")
    }
}