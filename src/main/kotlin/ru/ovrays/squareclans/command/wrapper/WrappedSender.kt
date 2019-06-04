package ru.ovrays.squareclans.command.wrapper

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.util.ClanPlayer
import ru.ovrays.squareclans.util.ColorUtilities

class WrappedSender(private val source: CommandSender) {
    enum class SenderType {
        CONSOLE, PLAYER
    }

    private val senderType: SenderType

    val isPlayer: Boolean
        get() = senderType == SenderType.PLAYER
    val isConsole: Boolean
        get() = senderType == SenderType.CONSOLE

    val sourcePlayer: ClanPlayer
        get() = if (isPlayer) ClanPlayer(source as Player) else throw RuntimeException("Source is NOT a player!")


    fun sendMessage(text: String) {
        val translated = ChatColor.translateAlternateColorCodes('&', text)

        if (senderType == SenderType.PLAYER) {
            sourcePlayer.sendMessage(translated)
        } else if (senderType == SenderType.CONSOLE) {
            SquareClansPlugin.sendToConsole(translated)
        }
    }

    fun hasPermission(note: String): Boolean {
        return if(isConsole)
            true
        else
            sourcePlayer.hasPermission(note)
    }

    fun error(string: String) = sendMessage(ColorUtilities.RED + ChatColor.translateAlternateColorCodes('8', string))

    init {
        senderType = if (source is Player) {
            SenderType.PLAYER
        } else {
            SenderType.CONSOLE
        }
    }
}