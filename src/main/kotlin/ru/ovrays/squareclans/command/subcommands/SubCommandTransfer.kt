package ru.ovrays.squareclans.command.subcommands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.model.Member
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.model.Rank
import ru.ovrays.squareclans.util.*


object SubCommandTransfer : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.ANY
    override val clanPermission: Permission? = Permission.INVITE_EXPEL_MEMBERS
    override val aliases: Collection<String> = setOf("transfer")
    override val commandPermission: String = "SquareClan.command.transfer"
    override val description: String = "�������� ����� ��������� ������� ��������� �����"
    override val usage: String = createUsage("transfer <�����/accept/deny>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if (args.size != 1)
            invalidArgsCountError(1, args.size)

        when(args[0]) {

            "accept" -> {

                val queue = SquareClansPlugin.leader_transfer_queue
                // Old = Value
                // New = Key

                if(!sender.sourcePlayer.isInClan()) {
                    sender.error("�� ������ �������� � �����!")
                    return
                }

                if(!queue.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("������ �� �������� ��������� �� �������")
                    return
                }

                val newOwner = sender.sourcePlayer
                val oldOwner = ClanPlayer(Bukkit.getPlayer(queue.getValue(newOwner.uniqueId)))

                val clan = newOwner.getClan()

                clan.owner = Member.Owner(newOwner.uniqueId, Rank.OWNER)
                queue.remove(newOwner.uniqueId, oldOwner.uniqueId)

                SquareClansPlugin.broadcastMessage("${ColorUtilities.WHITE}${newOwner.name}${ColorUtilities.GREEN} ���������� ����� ������� ����� ${ColorUtilities.WHITE}${clan.name}")
                clan.broadcastToMembers("${ColorUtilities.WHITE}${newOwner.name}${ColorUtilities.GREEN} ���������� ����� ������� ������ �����!")
            }

            "deny" -> {

                val queue = SquareClansPlugin.leader_transfer_queue
                // Old = Value
                // New = Key

                if(!sender.sourcePlayer.isInClan()) {
                    sender.error("�� ������ �������� � �����!")
                    return
                }

                if(!queue.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("������ �� �������� ��������� �� �������")
                    return
                }

                val newOwner = sender.sourcePlayer
                val oldOwner = Bukkit.getPlayer(queue.getValue(newOwner.uniqueId)) as ClanPlayer


                queue.remove(newOwner.uniqueId, oldOwner.uniqueId)
                sender.error("����������� ���� ���������.")
                oldOwner.sendMessage("${ColorUtilities.RED}����� ${ColorUtilities.WHITE}${newOwner.name}${ColorUtilities.RED} ��������� ���� �����������.")
            }

            else -> {

                val target = CommandUtilities.getPlayer(args.first())
                val targetClan = ClanUtilities.getClanByPlayer(target)

                val senderRank = sender.sourcePlayer.getMember().rank

                if (targetClan == null || targetClan.name.equals(sender.sourcePlayer.getClan().name, true).not()) {
                    sender.error("���� ����� �� ������� � ����� �����")
                    return
                }

                if (target.uniqueId == sender.sourcePlayer.uniqueId) {
                    sender.error("�� �� ������ �������� ��������� ���� ����")
                    return
                }

                if (senderRank != Rank.OWNER) {
                    sender.error("�� �� ��������� ���������� �����")
                    return
                }

                if (!target.isOnline) {
                    sender.error("����� ������ ���� ������")
                    return
                }

                if(SquareClansPlugin.invites.containsKey(target.uniqueId)) {
                    sender.error("������ ����� ��� ������� �����������")
                    return
                }

                SquareClansPlugin.leader_transfer_queue[target.uniqueId] = sender.sourcePlayer.uniqueId

                sender.sendMessage((ColorUtilities.YELLOW + "����������� ���������� ����������"))

                val m = ChatColor.translateAlternateColorCodes('&', ConfigLoader.getConfig().transferMessage)
                target.sendMessage(m.replace("%sender%", sender.sourcePlayer.name))

                SquareClansPlugin.buildTask(20 * 60) {
                    if (SquareClansPlugin.leader_transfer_queue.containsKey(target.uniqueId)) {
                        SquareClansPlugin.leader_transfer_queue.remove(target.uniqueId)
                        sender.error("������ �� �������� ��������� ������������� ���������.")
                    }
                }
            }

        }
    }
}