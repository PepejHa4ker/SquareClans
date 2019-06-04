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
    override val description: String = "Пригласить игрока в клан"
    override val usage: String = createUsage("invite <игрок/accept/deny>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if (args.size != 1)
            invalidArgsCountError(1, args.size)

        when(args[0]) {

            "accept" -> {
                if(sender.sourcePlayer.isInClan()) {
                    sender.error("Вы не должны состоять в клане!")
                    return
                }

                if(!SquareClansPlugin.invites.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("Приглашение не найдено")
                    return
                }

                val player = Bukkit.getServer().getPlayer(SquareClansPlugin.invites[sender.sourcePlayer.uniqueId]!!)
                val clan = ClanPlayer(player).getClan()


                if(clan.availableMembers() <= 0) {
                    sender.error("В клане нет свободных мест")
                    return
                }

                clan.broadcastToMembers((ColorUtilities.WHITE+sender.sourcePlayer.name)+(ColorUtilities.GREEN+" вступает в ваш клан!"))
                SquareClansPlugin.broadcastMessage((ColorUtilities.WHITE+sender.sourcePlayer.name)+(ColorUtilities.GREEN+" вступает в клан ")+(ColorUtilities.WHITE+clan.name))
                clan.addMember(sender.sourcePlayer.uniqueId)
                SquareClansPlugin.invites.remove(sender.sourcePlayer.uniqueId)
            }

            "deny" -> {
                if(sender.sourcePlayer.isInClan()) {
                    sender.error("Вы не должны состоять в клане!")
                    return
                }

                if(!SquareClansPlugin.invites.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("Приглашение не найдено")
                    return
                }

                Bukkit.getServer().getPlayer(SquareClansPlugin.invites.getValue(sender.sourcePlayer.uniqueId)).sendMessage((ColorUtilities.RED + "Приглашение игрока " + ColorUtilities.WHITE + sender.sourcePlayer.name + ColorUtilities.RED + " отклонено."))
                sender.error("Приглашение было отклонено.")
                SquareClansPlugin.invites.remove(sender.sourcePlayer.uniqueId)
                return

            }

            else -> {
                if(!sender.sourcePlayer.isInClan()) {
                    sender.error("Вы должны состоять в клане!")
                    return
                }

                val target = getPlayer(args.first())

                if (ClanUtilities.checkPlayer(target.uniqueId)) {
                    sender.error("Игрок уже состоит в клане")
                    return
                }

                if (sender.sourcePlayer.getClan().availableMembers() == 0) {
                    sender.error("В клане нет свободных мест")
                    return
                }

                if(SquareClansPlugin.invites.containsKey(target.uniqueId)) {
                    sender.error("Данный игрок уже получил приглашение")
                    return
                }

                val m = ChatColor.translateAlternateColorCodes('&', ConfigLoader.getConfig().inviteMessage)

                target.sendMessage(m.replace("%clan%", sender.sourcePlayer.getClan().name))
                sender.sendMessage((ColorUtilities.YELLOW + "Приглашение отправлено"))
                SquareClansPlugin.invites[target.uniqueId] = sender.sourcePlayer.uniqueId
                val clan = sender.sourcePlayer.getClan().name

                SquareClansPlugin.buildTask(20 * 60) {
                    if (SquareClansPlugin.invites.containsKey(target.uniqueId)) {
                        target.sendMessage((ColorUtilities.YELLOW + "Приглашение в клан ${clan} отклонено."))
                        sender.sendMessage((ColorUtilities.YELLOW + "Приглашение игрока ") + (ColorUtilities.WHITE + target.name) + (ColorUtilities.YELLOW + " отклонено."))
                        SquareClansPlugin.invites.remove(target.uniqueId)
                    }
                }
            }

        }
    }
}