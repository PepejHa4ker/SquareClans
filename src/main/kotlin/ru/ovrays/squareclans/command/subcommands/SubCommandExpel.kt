package ru.ovrays.squareclans.command.subcommands


import org.bukkit.Bukkit
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Member
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ColorUtilities
import ru.ovrays.squareclans.util.sendMessage

object SubCommandExpel : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.MEMBER
    override val clanPermission: Permission? = Permission.INVITE_EXPEL_MEMBERS
    override val aliases: Collection<String> = setOf("expel","kick")
    override val commandPermission: String = "SquareClans.command.expel"
    override val description: String = "Выгнать участника из клана"
    override val usage: String = createUsage("<expel/kick> <игрок>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if(args.size != 1)
            invalidArgsCountError(1, args.size)

        val senderClan = sender.sourcePlayer.getClan()

        val target = try { Bukkit.getOfflinePlayer(args[0]) } catch (e: Exception) { null }
        if(target == null) {
            sender.error("Игрок не существует!")
            return
        }

        val member: Member? = senderClan.members.find { it.id == target.uniqueId }
        if(member == null) {
            sender.error("Игрок не состоит в вашем клане!")
            return
        }

        val senderMember = sender.sourcePlayer.getMember()
        if(!senderMember.rank.canModify(member.rank)) {
            sender.error("Вы не можете изгнать этого игрока, так как его звание равно или выше вашего!")
            return
        }

        target.sendMessage((ColorUtilities.YELLOW + "Вы были исключены из клана " + ColorUtilities.RED + senderClan.name))
        senderClan.removeMember(target.uniqueId, sender.sourcePlayer)
        senderClan.broadcastToMembers((ColorUtilities.YELLOW + target.name + " был изгнан из вашего клана!"))
    }
}