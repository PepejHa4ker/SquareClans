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
    override val description: String = "Добавить очки игроку"
    override val usage: String = createUsage("points <add/remove> <игрок> <значение>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if(args.size < 3)
            this.invalidArgsCountError(3, args.size)

        when(args[0]) {

            "add" -> {
                val (member, toAdd) = getPlayerAndSum(args)
                sender.sendMessage("&aИгроку ${member.getPlayer().displayName}&a было добавлено ${getSuffixed(toAdd)}. Теперь очков у игрока: &c${member.points + toAdd}")
                member.addPoints(toAdd)
            }

            "remove" -> {
                val (member, toRemove) = getPlayerAndSum(args)
                val summary = member.removePoints(toRemove)
                sender.sendMessage("&aИгроку ${member.getPlayer().displayName}&a было удалено ${getSuffixed(toRemove)}. Теперь очков у игрока: &c$summary")
            }

            else -> {
                throw CommandError("Неизвестная операция: ${args[0]}", true)
            }
        }
    }

    private fun getPlayerAndSum(args: Array<String>): Pair<Member, Int> {
        val player = CommandUtilities.getPlayer(args[1])
        val cp =  try { ClanPlayer(player.player).getMember() } catch (e: Exception) { throw CommandError("Данный игрок не состоит в клане", false)}
        val numRaw = args[2]

        val num: Int = try { numRaw.toInt() } catch (e: Exception) { throw CommandError("Неверное число: $numRaw", false) }
        return cp to num
    }

    fun getSuffixed(int: Int): String {
        return when (int) {
            1 -> "&c$int &aочко"
            -1 -> "&c$int &aочко"
            in 2..4 -> "&c$int &aочка"
            in -2 downTo -4 -> "&c$int &aочка"
            else -> "&c$int &aочков"
        }
    }

}


