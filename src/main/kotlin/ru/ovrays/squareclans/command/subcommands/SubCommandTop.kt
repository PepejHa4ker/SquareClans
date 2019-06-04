package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ColorUtilities


object SubCommandTop : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = false
    override val memberRelation: MemberRelation = MemberRelation.ANY
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("top")
    override val commandPermission: String = "SquareClans.command.top"
    override val description: String = "��� ������"
    override val usage = createUsage("top [��������]")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if(Clan.clans.isEmpty()) {
            sender.error("�� ������� ��� ��� ������")
            return
        }

        var page = args.getOrNull(0)?.toIntOrNull() ?: 1
        val top = Clan.clans.sortedWith(compareBy<Clan> { it.level.number }.thenBy { it.points() }).reversed()

        sender.sendMessage(ColorUtilities.YELLOW + "���������, ���� ������� ����� " + ColorUtilities.RED+Clan.clans.size + ColorUtilities.YELLOW+" ������.")

        val max = if (10 * page > top.size) top.size else 10 * page
        page = Math.ceil(max.toDouble() / 10.0).toInt()
        val i = page * 10 - 9

        val str = StringBuilder().append(ColorUtilities.GREEN + "&8&m------------[-&r &a��� &8[&a$page&8/&a${Math.ceil(top.size.toDouble() / 10.0).toInt()}&8] &8&m-]------------\n")
        var position = i
        while (position <= max) {
            val clan = top[position - 1]
            str
                .append("&c$position. &a${clan.name} &8[&c${clan.level.number}&8 level  - &c${clan.points()} &8points]\n")
            ++position
        }
        sender.sendMessage(str.toString())
        sender.sendMessage(ColorUtilities.GREEN + "&8&m------------[-&r &8&m--------&r &8&m-]------------") }
}
