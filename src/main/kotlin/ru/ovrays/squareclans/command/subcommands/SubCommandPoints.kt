package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.CommandError
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Member
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ClanPlayer
import ru.ovrays.squareclans.util.CommandUtilities

object SubCommandPoints: SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = false
    override val memberRelation: MemberRelation = MemberRelation.MEMBER
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("points")
    override val commandPermission: String = "SquareClans.command.points"
    override val description: String = "�������� ���� ������"
    override val usage: String = createUsage("points <add/remove> <�����> <��������>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if(args.size < 3)
            this.invalidArgsCountError(3, args.size)

        when(args[0]) {

            "add" -> {
                val (member, toAdd) = getPlayerAndSum(args)
                sender.sendMessage("&a������ ${member.getPlayer().displayName}&a ���� ��������� ${getSuffixed(toAdd)}. ������ ����� � ������: &c${member.points + toAdd}")
                member.addPoints(toAdd)
            }

            "remove" -> {
                val (member, toRemove) = getPlayerAndSum(args)
                val summary = member.removePoints(toRemove)
                sender.sendMessage("&a������ ${member.getPlayer().displayName}&a ���� ������� ${getSuffixed(toRemove)}. ������ ����� � ������: &c$summary")
            }

            else -> {
                throw CommandError("����������� ��������: ${args[0]}", true)
            }
        }
    }

    private fun getPlayerAndSum(args: Array<String>): Pair<Member, Int> {
        val player = CommandUtilities.getPlayer(args[1])
        val cp =  try { ClanPlayer(player.player).getMember() } catch (e: Exception) { throw CommandError("������ ����� �� ������� � �����", false)}
        val numRaw = args[2]

        val num: Int = try { numRaw.toInt() } catch (e: Exception) { throw CommandError("�������� �����: $numRaw", false) }
        return cp to num
    }

    fun getSuffixed(int: Int): String {
        return when (int) {
            1 -> "&c$int &a����"
            -1 -> "&c$int &a����"
            in 2..4 -> "&c$int &a����"
            in -2 downTo -4 -> "&c$int &a����"
            else -> "&c$int &a�����"
        }
    }

}


