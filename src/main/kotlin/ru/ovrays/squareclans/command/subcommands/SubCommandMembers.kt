package ru.ovrays.squareclans.command.subcommands

import org.bukkit.Bukkit
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ClanUtilities
import ru.ovrays.squareclans.util.ColorUtilities


object SubCommandMembers : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = false
    override val memberRelation: MemberRelation = MemberRelation.ANY
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("members")
    override val commandPermission: String = "SquareClans.command.members"
    override val description: String = "Вывести список участников клана"
    override val usage: String = createUsage("members [клан]")
    override fun execute(sender: WrappedSender, args: Array<String>) {
        if (args.size > 1)
            SubCommandInfo.invalidArgsCountError(1, args.size)

        val optional = args.getOrNull(0)
        val clan: Clan

        clan = if (optional != null) {
            ClanUtilities.getClan(optional)!!
        } else {
            if (sender.isPlayer) {
                if (sender.sourcePlayer.isInClan()) {
                    sender.sourcePlayer.getClan()
                } else {
                    sender.error("Укажите клан")
                    return
                }
            } else {
                sender.error("Укажите клан")
                return
            }
        }

        sendMembersInfo(clan, sender)
    }

    private fun sendMembersInfo(clan: Clan, sender: WrappedSender) {

        sender.sendMessage("&8&m------------[ &a Участники &8&m ]-----------")

        clan.members.plus(clan.owner).forEach { member ->
        val rank = member.rank.localizedName
        val play = Bukkit.getOfflinePlayer(member.id)
        val name = play.name
        val stat = if (play.isOnline) ColorUtilities.GREEN + "Онлайн" else ColorUtilities.RED + "Оффлайн"
        sender.sendMessage(" &8- &9[&b$rank&9]&r $name &7($stat&7) &8| &c${member.points}")
        }
        sender.sendMessage("&8&m------------[ &r &8&m---------&r &8&m ]-----------")

    }
}