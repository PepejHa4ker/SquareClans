package ru.ovrays.squareclans.util

import org.bukkit.entity.Player
import ru.ovrays.squareclans.model.Permission

open class ClanPlayer(player: Player) : Player by player {
    fun isInClan() = ClanUtilities.checkPlayer(this.uniqueId)
    fun getClan() = ClanUtilities.getClanByPlayer(this)!!
    fun getMember() = getClan().members.plus(getClan().owner).find { it.id == this.uniqueId }!!
    fun hasClanPermission(permission: Permission) : Boolean = this.getMember().rank.hasPermission(permission)
}