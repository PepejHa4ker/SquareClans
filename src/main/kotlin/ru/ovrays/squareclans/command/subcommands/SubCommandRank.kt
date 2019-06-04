package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.CommandError
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.*


object SubCommandRank : SubCommand() {
    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.MEMBER
    override val clanPermission: Permission? = Permission.MODIFY_MEMBERS
    override val aliases: Collection<String> = setOf("rank")
    override val commandPermission: String = "SquareClans.command.rank"
    override val description: String = "Изменить звание игрока"
    override val usage: String = createUsage("rank <set/up/down> <игрок> <звание>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if (args.size < 2) {
            invalidArgsCountError(2, args.size)
        }

        when(args[0]) {

            "set" -> {

                if (args.size != 3) {
                    invalidArgsCountError(3, args.size)
                }

                val target = CommandUtilities.getPlayer(args[1])
                val target_rank = CommandUtilities.getRank(args[2])
                val target_clan = ClanUtilities.getClanByPlayer(target)

                val src = sender.sourcePlayer
                val src_rank = src.getMember().rank
                val src_clan = ClanUtilities.getClanByPlayer(src)

                if (target_clan == null || target_clan.name != src_clan!!.name) {
                    sender.error("Данный игрок не состоит в вашем клане")
                    return
                }

                if (target.uniqueId == src.uniqueId) {
                    sender.error("Вы не можете изменить своё звание")
                    return
                }

                if (src_rank == target_rank) {
                    sender.error("Вы не можете выдавать звание, равное вашему.")
                    return
                }

                if(!src_rank.canModify(target_rank)) {
                    sender.error("Вы не можете изменять звание игрока, чьё звание выше вашего")
                    return
                }

                if(target_rank == ClanPlayer(target.player).getMember().rank) {
                    sender.error("Этот игрок уже имеет данный ранг")
                }

                ClanPlayer(target.player).getMember().rank = target_rank
                sender.sourcePlayer.sendMessage((ColorUtilities.GREEN + "Игроку ${target.player.name} было установлено звание: ${target_rank.localizedName}"))
                target.sendMessage((ColorUtilities.GREEN + "Ваше звание было изменено на: ${target_rank.localizedName}"))

                DataLoader.save()

            }

            "up" -> {

                if (args.size != 2) {
                    invalidArgsCountError(2, args.size)
                }

                val target = CommandUtilities.getPlayer(args[1])

                if (CommandUtilities.isRankMax(ClanPlayer(target.player).getMember().rank)) {
                    sender.error("Вы не можете изменить звание игрока, так-как оно уже максимальное.")
                    return
                }

                val target_rank = CommandUtilities.getRank(ClanPlayer(target.player).getMember().rank.priority + 1)
                val target_clan = ClanUtilities.getClanByPlayer(target)

                val src = sender.sourcePlayer
                val src_rank = src.getMember().rank
                val src_clan = ClanUtilities.getClanByPlayer(src)

                if (target_clan == null || target_clan.name != src_clan!!.name) {
                    sender.error("Данный игрок не состоит в вашем клане")
                    return
                }

                if (target.uniqueId == src.uniqueId) {
                    sender.error("Вы не можете изменить своё звание")
                    return
                }

                if(!src_rank.canModify(target_rank)) {
                    sender.error("Вы не можете изменять звание игрока, чьё звание выше вашего")
                    return
                }

                if (src_rank == target_rank) {
                    sender.error("Вы не можете выдавать звание, равное вашему.")
                    return
                }

                ClanPlayer(target.player).getMember().rank = target_rank
                sender.sourcePlayer.sendMessage((ColorUtilities.GREEN + "Игроку ${target.player.name} было установлено звание: ${target_rank.localizedName}"))
                target.sendMessage((ColorUtilities.GREEN + "Ваше звание было изменено на: ${target_rank.localizedName}"))

                DataLoader.save()
            }

            "down" -> {

                if (args.size != 2) {
                    invalidArgsCountError(2, args.size)
                }

                val target = CommandUtilities.getPlayer(args[1])

                if (CommandUtilities.isRankMin(ClanPlayer(target.player).getMember().rank)) {
                    sender.error("Вы не можете изменить звание игрока, так-как оно уже минимальное.")
                    return
                }

                val target_rank = CommandUtilities.getRank(ClanPlayer(target.player).getMember().rank.priority - 1)
                val target_clan = ClanUtilities.getClanByPlayer(target)

                val src = sender.sourcePlayer
                val src_rank = src.getMember().rank
                val src_clan = ClanUtilities.getClanByPlayer(src)

                if (target_clan == null || target_clan.name != src_clan!!.name) {
                    sender.error("Данный игрок не состоит в вашем клане")
                    return
                }

                if (target.uniqueId == src.uniqueId) {
                    sender.error("Вы не можете изменить своё звание")
                    return
                }

                if(!src_rank.canModify(target_rank)) {
                    sender.error("Вы не можете изменять звание игрока, чьё звание выше вашего")
                    return
                }

                if (src_rank == target_rank) {
                    sender.error("Вы не можете изменять звание игрока, которе равно вашему.")
                    return
                }

                ClanPlayer(target.player).getMember().rank = target_rank
                sender.sourcePlayer.sendMessage((ColorUtilities.GREEN + "Игроку ${target.player.name} было установлено звание: ${target_rank.localizedName}"))
                target.sendMessage((ColorUtilities.GREEN + "Ваше звание было изменено на: ${target_rank.localizedName}"))

                DataLoader.save()
            }

            else -> {
                throw CommandError("Неизвестная операция: ${args[0]}", true)

            }
        }
    }
}
