package ru.ovrays.squareclans.model


import org.bukkit.Bukkit
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.util.ClanPlayer
import java.util.*
import kotlin.properties.Delegates
open class Member {

    val id: UUID
    var rank: Rank
    var points: Int by Delegates.observable(0) {prop, new, old -> }
    var clanChat: Boolean by Delegates.observable(false) {prop, new, old -> }
    fun getPlayer() = Bukkit.getServer().getPlayer(id)

    constructor(id: UUID, rank: Rank, points: Int, clanChat: Boolean) {
        this.id = id
        this.rank = rank
        this.points = points
        this.clanChat = clanChat
    }

    fun addPoints(amount: Int): Int {
        val p =  Bukkit.getPlayer(id)
        this.points += amount

        ClanPlayer(p).getClan().addPoints(amount, p)
        DataLoader.save()
        return this.points
    }

    fun removePoints(amount: Int): Int {

        val p = Bukkit.getPlayer(id)

        this.points -= amount
        ClanPlayer(p).getClan().removePoints(amount, p)
        DataLoader.save()
        return this.points
    }

    fun switchChat(to: Boolean? = null) {
        if(to == null)
            this.clanChat = !this.clanChat
        else
            this.clanChat = to
        DataLoader.save()
    }


    override fun toString(): String {
        return "Member@[id$id],rank=$rank,points=$points,clanChat=$clanChat]"
    }

    class Owner(id: UUID, rank: Rank) : Member(id, Rank.OWNER,0, false) {
        constructor(id: UUID) : this(id, Clan.defaultRole)
    }
}
