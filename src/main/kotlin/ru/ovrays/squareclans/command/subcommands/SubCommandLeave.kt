package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.model.Rank
import ru.ovrays.squareclans.util.ColorUtilities


object SubCommandLeave : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.MEMBER
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("leave")
    override val commandPermission: String = "SquareClans.command.leave"
    override val description: String = "Покинуть клан"
    override val usage: String = createUsage("leave")
    override fun execute(sender: WrappedSender, args: Array<String>) {
        if(sender.sourcePlayer.getMember().rank == Rank.OWNER) {
            sender.error("Лидер не может покинуть свой клан.")
            return
        }
        val clan = sender.sourcePlayer.getClan()
        clan.removeMember(sender.sourcePlayer.uniqueId, sender.sourcePlayer)
        sender.sendMessage((ColorUtilities.YELLOW + "Вы покинули клан " + ColorUtilities.RED + clan.name))
        clan.broadcastToMembers((ColorUtilities.YELLOW + sender.sourcePlayer.name + " покидает ваш клан"))
    }
}