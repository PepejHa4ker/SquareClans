package ru.ovrays.squareclans.command

import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.command.wrapper.CommandSpec
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ColorUtilities


object CommandClanChat {
    fun build(): CommandSpec {
        val builder = CommandSpec.builder()
                .permission("SquareClans.command.chat")
                .description("SquareClans ClanChat")
                .usage("cc <сообщение...> / on / off")
                .executor { src, args ->

                    if (!src.isPlayer) {
                        src.error("Только от лица игрока")
                        return@executor
                    }

                    val player = src.sourcePlayer
                    if (!player.isInClan()) {
                        src.error("Вы должны состоять в клане")
                        return@executor
                    }

                    if (!player.hasClanPermission(Permission.CLAN_CHAT_WRITE)) {
                        src.error("У вас нет прав на это")
                        return@executor
                    }

                    when {
                        args.isEmpty() -> {
                            if(player.getMember().clanChat) src.sendMessage("${ColorUtilities.YELLOW}Вы вышли из клан-чата")
                            else src.sendMessage("${ColorUtilities.YELLOW}Вы перешли в клан-чат")
                            player.getMember().switchChat()

                        }

                        args[0].equals("on", true) -> {
                            player.getMember().switchChat(true)
                            src.sendMessage("${ColorUtilities.YELLOW}Вы перешли в клан-чат")
                        }

                        args[0].equals("off", true) -> {
                            player.getMember().switchChat(false)
                            src.sendMessage("${ColorUtilities.YELLOW}Вы вышли из клан-чата")
                        }

                        else -> {
                            val message = args.joinToString(" ")
                            val finText = "${ConfigLoader.getConfig().clanChatTag} ${player.displayName}&r: ${message}"
                            player.getClan().broadcastToMembers(finText)
                            SquareClansPlugin.sendToConsole("[ClanChat][${player.getClan().name}] ${player.displayName}: ${message.replace("§", "&")}")
                            SquareClansPlugin.sendToModerators(message, player)
                            return@executor
                            }
                        }

                }
        return builder.build()

    }
}