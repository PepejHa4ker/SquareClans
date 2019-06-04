package ru.ovrays.squareclans.command.subcommands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ClanPlayer
import ru.ovrays.squareclans.util.ClanUtilities
import ru.ovrays.squareclans.util.ColorUtilities
import ru.ovrays.squareclans.util.CommandUtilities.getPlayer
import ru.ovrays.squareclans.util.sendMessage


object SubCommandInvite : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.ANY
    override val clanPermission: Permission? = Permission.INVITE_EXPEL_MEMBERS
    override val aliases: Collection<String> = setOf("invite")
    override val commandPermission: String = "SquareClan.command.invite"
    override val description: String = "���������� ������ � ����"
    override val usage: String = createUsage("invite <�����/accept/deny>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if (args.size != 1)
            invalidArgsCountError(1, args.size)

        when(args[0]) {

            "accept" -> {
                if(sender.sourcePlayer.isInClan()) {
                    sender.error("�� �� ������ �������� � �����!")
                    return
                }

                if(!SquareClansPlugin.invites.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("����������� �� �������")
                    return
                }

                val player = Bukkit.getServer().getPlayer(SquareClansPlugin.invites[sender.sourcePlayer.uniqueId]!!)
                val clan = ClanPlayer(player).getClan()


                if(clan.availableMembers() <= 0) {
                    sender.error("� ����� ��� ��������� ����")
                    return
                }

                clan.broadcastToMembers((ColorUtilities.WHITE+sender.sourcePlayer.name)+(ColorUtilities.GREEN+" �������� � ��� ����!"))
                SquareClansPlugin.broadcastMessage((ColorUtilities.WHITE+sender.sourcePlayer.name)+(ColorUtilities.GREEN+" �������� � ���� ")+(ColorUtilities.WHITE+clan.name))
                clan.addMember(sender.sourcePlayer.uniqueId)
                SquareClansPlugin.invites.remove(sender.sourcePlayer.uniqueId)
            }

            "deny" -> {
                if(sender.sourcePlayer.isInClan()) {
                    sender.error("�� �� ������ �������� � �����!")
                    return
                }

                if(!SquareClansPlugin.invites.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("����������� �� �������")
                    return
                }

                Bukkit.getServer().getPlayer(SquareClansPlugin.invites.getValue(sender.sourcePlayer.uniqueId)).sendMessage((ColorUtilities.RED + "����������� ������ " + ColorUtilities.WHITE + sender.sourcePlayer.name + ColorUtilities.RED + " ���������."))
                sender.error("����������� ���� ���������.")
                SquareClansPlugin.invites.remove(sender.sourcePlayer.uniqueId)
                return

            }

            else -> {
                if(!sender.sourcePlayer.isInClan()) {
                    sender.error("�� ������ �������� � �����!")
                    return
                }

                val target = getPlayer(args.first())

                if (ClanUtilities.checkPlayer(target.uniqueId)) {
                    sender.error("����� ��� ������� � �����")
                    return
                }

                if (sender.sourcePlayer.getClan().availableMembers() == 0) {
                    sender.error("� ����� ��� ��������� ����")
                    return
                }

                if(SquareClansPlugin.invites.containsKey(target.uniqueId)) {
                    sender.error("������ ����� ��� ������� �����������")
                    return
                }

                val m = ChatColor.translateAlternateColorCodes('&', ConfigLoader.getConfig().inviteMessage)

                target.sendMessage(m.replace("%clan%", sender.sourcePlayer.getClan().name))
                sender.sendMessage((ColorUtilities.YELLOW + "����������� ����������"))
                SquareClansPlugin.invites[target.uniqueId] = sender.sourcePlayer.uniqueId
                val clan = sender.sourcePlayer.getClan().name

                SquareClansPlugin.buildTask(20 * 60) {
                    if (SquareClansPlugin.invites.containsKey(target.uniqueId)) {
                        target.sendMessage((ColorUtilities.YELLOW + "����������� � ���� ${clan} ���������."))
                        sender.sendMessage((ColorUtilities.YELLOW + "����������� ������ ") + (ColorUtilities.WHITE + target.name) + (ColorUtilities.YELLOW + " ���������."))
                        SquareClansPlugin.invites.remove(target.uniqueId)
                    }
                }
            }

        }
    }
}