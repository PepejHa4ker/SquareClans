package ru.ovrays.squareclans.command

import ru.ovrays.squareclans.command.subcommands.*

import ru.ovrays.squareclans.command.wrapper.CommandSpec


object CommandClan {

    val subcommands: List<SubCommand> = arrayOf(
        SubCommandCreate,
        SubCommandDelete,
        SubCommandHelp,
        SubCommandInvite,
        SubCommandInfo,
        SubCommandRank,
        SubCommandLeave,
        SubCommandExpel,
        SubCommandMembers,
        SubCommandReload,
        SubCommandPoints,
        SubCommandPrefix,
        SubCommandTop,
        SubCommandTransfer,
        SubCommandBalance
    ).toList()

    fun build(): CommandSpec {

        val cmdMap = HashMap<List<String>, CommandSpec>()
        subcommands.forEach {
            cmdMap[it.aliases.toList()] = it.parseCommandSpec()
        }

        val builder = CommandSpec.builder()
            .permission("SquareClans.command.base")
            .description("SquareClans base command")
            .children(cmdMap)
            .executor { src, args ->
                if (args.isNotEmpty()) {
                    src.error("Неизвестная команда. Введите /c help")
                    return@executor
                }

            src.sendMessage("&8&m----------[ &a SquareClans &8&m ]---------")
            src.sendMessage("&8 > Система кланов для &cSquareland KitPvP")
            src.sendMessage("&8 > Авторы: &cHellbog")
            src.sendMessage("&8 > Помощь: &c/c help")
            src.sendMessage("&8&m----------[ &r &8&m----------&r &8&m ]---------")

            }


        return builder.build()
    }
}