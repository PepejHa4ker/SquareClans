package ru.ovrays.squareclans.command.subcommands

import org.bukkit.ChatColor
import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.VaultHandler
import ru.ovrays.squareclans.command.wrapper.CommandError
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.model.Prefix
import ru.ovrays.squareclans.util.ClanUtilities
import ru.ovrays.squareclans.util.ColorUtilities
import ru.ovrays.squareclans.util.getFirstEqualsOrNull
import java.text.NumberFormat


object SubCommandPrefix : SubCommand() {

    val format: NumberFormat = NumberFormat.getInstance().apply {
        this.isGroupingUsed = true
    }

    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.MEMBER
    override val clanPermission: Permission? = Permission.MODIFY
    override val aliases: Collection<String> = setOf("prefix")
    override val commandPermission: String = "SquareClans.command.prefix"
    override val description: String = "���������� �������� �������"
    override val usage: String = createUsage("prefix <set/unset> <�������> <����>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        val clan = sender.sourcePlayer.getClan()

        val balance = VaultHandler.econ.getBalance(sender.sourcePlayer)
        val price = ConfigLoader.getConfig().prefixChangeCost.toDouble()
        val isMoneyEnough = VaultHandler.econ.has(sender.sourcePlayer, price)

        if (args.size != 3)
            invalidArgsCountError(3, args.size)

        when(args[0]) {

            "set" -> {
                if (clan.level.number < 3) {
                    sender.error("��� ���� ��� �� ������ 3 ������")
                    return
                }

                val prefix_text = args[1]
                val prefix_color = try { SquareClansPlugin.color_strings.getValue(args[2].toLowerCase()) } catch (e: Exception) { throw CommandError("����������� ����: ${args[2]}", false) }

                val prefix = Prefix(prefix_color, prefix_text)

                if (!prefix.isValid) {
                    sender.error(ConfigLoader.getConfig().prefixIsInvalid)
                    return
                }

                if(prefix.color !in clan.level.colors) {
                    sender.error("������ ���� �� �������� ������ �����!")
                    SquareClansPlugin.sendToConsole("${prefix.color.name} not in ${clan.level.colors.map(ChatColor::name)}")
                    return
                }

                if (ClanUtilities.prefixExists(prefix) && clan != prefix.getFirstEqualsOrNull()!!) {
                    sender.error("���� ������� ��� �����")
                    return
                }

                if(ConfigLoader.getConfig().clanCreateCost != 0) {
                    if (!isMoneyEnough) {
                        sender.error("������������ ������� &8(${balance.toString().removeSuffix(".0")}/${format.format(price)})")
                        return
                    }
                    val withdrawResult = VaultHandler.econ.withdrawPlayer(sender.sourcePlayer, ConfigLoader.getConfig().prefixChangeCost.toDouble())
                    if (!withdrawResult.transactionSuccess()) {
                        sender.error("������ ��� �������� �������: ${withdrawResult.errorMessage}")
                        return
                    }

                    sender.sendMessage("&e� ������ �������� ���� �������: ${format.format(price)}$")
                }

                sender.sourcePlayer.getClan().setPrefix(prefix)
                sender.sourcePlayer.getClan().broadcastToMembers((ColorUtilities.GREEN + "������� ������ ����� ������ �� ${prefix.color}${prefix.text}"))
                DataLoader.save()
            }

            "unset" -> {
                clan.prefix = null

                val reward = ConfigLoader.getConfig().prefixDeleteReward
                sender.sendMessage(ColorUtilities.RED + "������� ������ ����� ��� ������.")
                if (reward != 0) {
                    VaultHandler.econ.depositPlayer(sender.sourcePlayer, reward.toDouble())
                    sender.sendMessage((ColorUtilities.GREEN + "��� ��������� ${format.format(reward)}$ � �������� ����������� �� �������� ��������."))
                }
                return
            }
        }
    }
}