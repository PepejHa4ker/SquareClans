package ru.ovrays.squareclans.command.subcommands


import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.VaultHandler
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ColorUtilities
import java.text.NumberFormat


object SubCommandDelete : SubCommand() {

    val format: NumberFormat = NumberFormat.getInstance().apply {
        this.isGroupingUsed = true
    }

    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.MEMBER
    override val clanPermission: Permission? = Permission.DELETE
    override val aliases: Collection<String> = setOf("delete")
    override val commandPermission: String = "SquareClans.command.delete"
    override val description: String = "Удалить клан"
    override val usage: String = createUsage("delete <accept/deny>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if (args.isNotEmpty()) {

            if (args[0] == "accept") {
                    if (!SquareClansPlugin.clan_delete_queue.containsKey(sender.sourcePlayer.uniqueId)) {
                        sender.error("Заявка на удаление клана не найдена.")
                        return
                    }

                    SquareClansPlugin.clan_delete_queue.remove(sender.sourcePlayer.uniqueId)
                    val clan = sender.sourcePlayer.getClan()
                    val matching = Clan.clans.filter { it.owner.id == sender.sourcePlayer.uniqueId }

                    if (matching.isEmpty()) {
                        sender.error("Ошибка при удалении клана")
                        return
                    }

                    synchronized(Clan.clans) {
                        Clan.clans.removeAll(matching)
                    }

                    clan.broadcastToMembers((ColorUtilities.RED + "Ваш клан распущен."))
                    DataLoader.save()
                    val reward = ConfigLoader.getConfig().clanDeleteReward

                    if(reward != 0) {
                        VaultHandler.econ.depositPlayer(sender.sourcePlayer, reward.toDouble())
                        sender.sendMessage((ColorUtilities.GREEN + "Вам начислено ${format.format(reward)}$ в качестве компенсации за удаление клана"))
                    }
                        return

                } else if (args[0] == "deny") {

                    if (!SquareClansPlugin.clan_delete_queue.containsKey(sender.sourcePlayer.uniqueId)) {
                        sender.error("Заявка на удаление клана не найдена.")
                        return
                    }

                    SquareClansPlugin.clan_delete_queue.remove(sender.sourcePlayer.uniqueId)
                    sender.error("Заявка на удаление клана была отклонена.")
                    return
                }

            }  else {

            if(!SquareClansPlugin.clan_delete_queue.containsKey(sender.sourcePlayer.uniqueId)) {
                SquareClansPlugin.clan_delete_queue[sender.sourcePlayer.uniqueId] = sender.sourcePlayer.getClan()
                sender.sendMessage(ColorUtilities.GREEN + "Введите " + ColorUtilities.WHITE + "/c delete accept" + ColorUtilities.GREEN + ", чтобы подтвердить удаление.")
                sender.sendMessage(ColorUtilities.GREEN + "Заявка будет автоматически отклонена через 15 секунд.")
                SquareClansPlugin.buildTask(20 * 15) {
                    if (SquareClansPlugin.clan_delete_queue.containsKey(sender.sourcePlayer.uniqueId)) {
                        SquareClansPlugin.clan_delete_queue.remove(sender.sourcePlayer.uniqueId)
                        sender.error("Заявка на удаление клана была автоматически отклонена.")
                    }
                }
            } else {
                sender.error("Вы уже подали заявку на удаление клана!")
            }
        }
    }
}

