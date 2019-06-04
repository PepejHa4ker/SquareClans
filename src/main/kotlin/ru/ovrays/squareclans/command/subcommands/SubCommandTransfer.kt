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
    override val description: String = "Передать права лидерства другому участнику клана"
    override val usage: String = createUsage("transfer <игрок/accept/deny>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if (args.size != 1)
            invalidArgsCountError(1, args.size)

        when(args[0]) {

            "accept" -> {

                val queue = SquareClansPlugin.leader_transfer_queue
                // Old = Value
                // New = Key

                if(!sender.sourcePlayer.isInClan()) {
                    sender.error("Вы должны состоять в клане!")
                    return
                }

                if(!queue.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("Заявка на передачу лидерства не найдена")
                    return
                }

                val newOwner = sender.sourcePlayer
                val oldOwner = ClanPlayer(Bukkit.getPlayer(queue.getValue(newOwner.uniqueId)))

                val clan = newOwner.getClan()

                clan.owner = Member.Owner(newOwner.uniqueId, Rank.OWNER)
                queue.remove(newOwner.uniqueId, oldOwner.uniqueId)

                SquareClansPlugin.broadcastMessage("${ColorUtilities.WHITE}${newOwner.name}${ColorUtilities.GREEN} становится новым лидером клана ${ColorUtilities.WHITE}${clan.name}")
                clan.broadcastToMembers("${ColorUtilities.WHITE}${newOwner.name}${ColorUtilities.GREEN} становится новым лидером вашего клана!")
            }

            "deny" -> {

                val queue = SquareClansPlugin.leader_transfer_queue
                // Old = Value
                // New = Key

                if(!sender.sourcePlayer.isInClan()) {
                    sender.error("Вы должны состоять в клане!")
                    return
                }

                if(!queue.containsKey(sender.sourcePlayer.uniqueId)) {
                    sender.error("Заявка на передачу лидерства не найдена")
                    return
                }

                val newOwner = sender.sourcePlayer
                val oldOwner = Bukkit.getPlayer(queue.getValue(newOwner.uniqueId)) as ClanPlayer


                queue.remove(newOwner.uniqueId, oldOwner.uniqueId)
                sender.error("Приглашение было отклонено.")
                oldOwner.sendMessage("${ColorUtilities.RED}Игрок ${ColorUtilities.WHITE}${newOwner.name}${ColorUtilities.RED} отклоняет ваше предложение.")
            }

            else -> {

                val target = CommandUtilities.getPlayer(args.first())
                val targetClan = ClanUtilities.getClanByPlayer(target)

                val senderRank = sender.sourcePlayer.getMember().rank

                if (targetClan == null || targetClan.name.equals(sender.sourcePlayer.getClan().name, true).not()) {
                    sender.error("Этот игрок не состоит в вашем клане")
                    return
                }

                if (target.uniqueId == sender.sourcePlayer.uniqueId) {
                    sender.error("Вы не можете передать лидерство сами себе")
                    return
                }

                if (senderRank != Rank.OWNER) {
                    sender.error("Вы не являетесь владельцем клана")
                    return
                }

                if (!target.isOnline) {
                    sender.error("Игрок должен быть онлайн")
                    return
                }

                if(SquareClansPlugin.invites.containsKey(target.uniqueId)) {
                    sender.error("Данный игрок уже получил предложение")
                    return
                }

                SquareClansPlugin.leader_transfer_queue[target.uniqueId] = sender.sourcePlayer.uniqueId

                sender.sendMessage((ColorUtilities.YELLOW + "Предложение отправлено отправлено"))

                val m = ChatColor.translateAlternateColorCodes('&', ConfigLoader.getConfig().transferMessage)
                target.sendMessage(m.replace("%sender%", sender.sourcePlayer.name))

                SquareClansPlugin.buildTask(20 * 60) {
                    if (SquareClansPlugin.leader_transfer_queue.containsKey(target.uniqueId)) {
                        SquareClansPlugin.leader_transfer_queue.remove(target.uniqueId)
                        sender.error("Заявка на передачу лидерства автоматически отклонена.")
                    }
                }
            }

        }
    }
}