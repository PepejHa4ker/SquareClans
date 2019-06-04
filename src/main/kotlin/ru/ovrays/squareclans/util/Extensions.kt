package ru.ovrays.squareclans.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.event.AbstractEvent
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Prefix

typealias EventClass = Class<out AbstractEvent>
typealias Executor = (src: WrappedSender, args: Array<String>) -> Unit
fun OfflinePlayer.sendMessage(msg: String) {
    if(this.isOnline)
        Bukkit.getPlayer(this.uniqueId).sendMessage(ChatColor.translateAlternateColorCodes('&', msg))
}

fun Prefix.getFirstEqualsOrNull()= Clan.clans.firstOrNull { it.prefix == this }
