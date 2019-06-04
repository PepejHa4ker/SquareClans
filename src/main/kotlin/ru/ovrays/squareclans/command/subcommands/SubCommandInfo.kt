package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ClanUtilities
import ru.ovrays.squareclans.util.ClanUtilities.getPrefixText

object SubCommandInfo : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = false
    override val memberRelation: MemberRelation = MemberRelation.ANY
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("info")
    override val commandPermission: String = "SquareClans.command.info"
    override val description: String = "������� ���������� � �����"
    override val usage: String = createUsage("info <����>")

    private fun sendInfo(clan: Clan, sender: WrappedSender) {

        val info = arrayListOf("&8&m----------[ &a ���������� � ����� &8&m ]---------",
            "&8��������: &c${clan.name}",
            "&8�������: &c${getPrefixText(clan)}", "&8���������: &c${clan.members.size + 1}",
            "&8�������: &c${clan.level.number}", "&8����: &c${clan.points()}",
            "&8&m----------[ &r &8&m-----------------&r &8&m ]---------")

        info.forEach {
            sender.sendMessage(it)
        }
    }

    override fun execute(sender: WrappedSender, args: Array<String>) {

        if (args.size > 1)
            invalidArgsCountError(1, args.size)

        val optional = args.getOrNull(0)
        val clan: Clan

        clan = if (optional != null) {
            ClanUtilities.getClan(optional)!!
        } else {
            if (sender.isPlayer) {
                if (sender.sourcePlayer.isInClan()) {
                    sender.sourcePlayer.getClan()
                } else {
                    sender.error("������� ����")
                    return
                }
            } else {
                sender.error("������� ����")
                return
            }
        }

        sendInfo(clan, sender)
    }
}
