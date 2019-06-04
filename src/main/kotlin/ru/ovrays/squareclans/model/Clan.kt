package ru.ovrays.squareclans.model

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.api.*
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.util.ColorUtilities
import java.util.*
import kotlin.properties.Delegates

class Clan private constructor(var name: String, var owner: Member.Owner) {


    companion object {
        val clans = LinkedList<Clan>()
        var namePattern = Regex(ConfigLoader.getConfig().clanRegexPattern + "{${ConfigLoader.getConfig().clanMinLength},${ConfigLoader.getConfig().clanMaxLength}}")
        val defaultRole = Rank.MEMBER
    }

    init {
        namePattern = Regex(ConfigLoader.getConfig().clanRegexPattern + "{${ConfigLoader.getConfig().clanMinLength},${ConfigLoader.getConfig().clanMaxLength}}")
    }

    override fun toString(): String {
        return "Clan@[name=$name,level=${this.level.number},points=$points,members=${members.size + 1}]"
    }

    var members = LinkedList<Member>()

    fun availableMembers(): Int = (level.members - members.size)
    private var points: Int by Delegates.observable(0) { prop, new, old ->  }

    fun points() = this.points

    var level: ClanLevel by Delegates.observable(ClanLevel.First_Level) { prop, new, old ->   }
    var prefix: Prefix? = null

    constructor(name: String, owner: UUID) : this(name, Member.Owner(owner, Rank.OWNER))
    constructor(
        name: String,
        owner: Member.Owner,
        prefix: Prefix?,
        level: ClanLevel,
        points: Int
    ) : this(name, owner) {
        this.prefix = prefix
        this.level = level
        this.points = points
    }


    fun addMember(id: UUID) {
        synchronized(Clan.clans) {
            if(members.count { it.id == id } == 0)
                this.members.add(Member(id, Rank.MEMBER, 0, false))
        }
        DataLoader.save()
        EventBus.post(ClanAddMemberEvent(Member(id, Rank.MEMBER, 0, false)))
    }

    fun removeMember(id: UUID, playerCause: Player) {
        val member = this.members.find { it.id == id }
        EventBus.post(ClanRemoveMemberEvent(member!!))
        this.removePoints(member.points, playerCause)
        synchronized(Clan.clans) {
            this.members.remove(member)
        }

        DataLoader.save()
    }

    fun addPoints(amount: Int, playerCause: Player?) {
        synchronized(Clan.clans) {
            this@Clan.points += amount
            EventBus.post(ClanExperienceGainEvent(amount, playerCause!!, this@Clan))
        }
        DataLoader.save()
    }

    fun removePoints(amount: Int, playerCause: Player?) {
        synchronized(Clan.clans) {
            this.points -= amount
            EventBus.post(ClanExperienceRemoveEvent(amount, playerCause!!, this@Clan))
        }
        DataLoader.save()
    }

    private fun isValidPoints(i: Int): Boolean {
        return i in 0..1200
    }

    fun setPoints(amount: Int, playerCause: Player?): Boolean {
        return if (this.isValidPoints(amount)) {
            synchronized(Clan.clans) {
                this.points = amount
                EventBus.post(ClanExperienceSetEvent(amount, playerCause!!, this@Clan))
            }
            DataLoader.save()
            true
        } else {
            false
        }
    }

    fun updateLevel(level: Int) {

        val oldLevel = ClanLevel.getLevel(level - 1)!!
        val newLevel = ClanLevel.getLevel(level)!!

        broadcastToMembers(ColorUtilities.GREEN + "Ваш клан получил уровень $level! Поздравляем!")
        val newColors = newLevel.newColors

        if (newColors.isNotEmpty()) {

            broadcastToMembers(ColorUtilities.GREEN + "Теперь вашему клану доступны следующие цвета:")

            val message = arrayListOf<String>()
            newColors.forEach {
                message.add("$it ${SquareClansPlugin.colors[it]}")
            }

            broadcastToMembers(
                message.toString().removePrefix("[").removeSuffix("]"))
        }

        if(oldLevel.members < newLevel.members) {
            broadcastToMembers(ColorUtilities.GREEN + "Теперь мест в вашем клане: ${ColorUtilities.AQUA}${newLevel.members}")
        }

        EventBus.post(ClanLevelUpEvent(oldLevel, newLevel))
        this.level = newLevel
        setPoints(0, null)
        DataLoader.save()
    }

    fun setPrefix(prefix: Prefix): Boolean{
        return if (prefix.isValid && level.colors.contains(prefix.color)) {
            synchronized(Clan.clans) {
                this.prefix = prefix
                DataLoader.save()
            }
            true
        } else {
            false
        }
    }

    fun broadcastToMembers(message: String) {
            members.plus(owner).forEach { member ->
                val user = Bukkit.getPlayer(member.id)
                if(user != null && user.isOnline) {
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                }
            EventBus.post(ClanBroadcastEvent(message))
        }
    }

}

