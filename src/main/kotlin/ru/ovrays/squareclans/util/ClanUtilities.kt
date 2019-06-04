package ru.ovrays.squareclans.util

import org.bukkit.OfflinePlayer
import ru.ovrays.squareclans.SquareClansPlugin.Companion.sendToConsole
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Member
import ru.ovrays.squareclans.model.Prefix
import java.util.*


object ClanUtilities {

    fun getTop(element: Int): Clan? {
        val rawTop = Clan.clans.sortedWith(compareBy<Clan?>
            { it!!.level.number }.thenBy
            { it!!.level.number }).reversed().take(10)

        val top = List(10) { rawTop.getOrNull(it) }
        return top.getOrNull(element - 1)
    }

    fun findPlayerByClan(clan: Clan, id: UUID): Member? = clan.members.plus(clan.owner).find { it.id == id }

    fun findPlayerAndClan(id: UUID): Pair<Member, Clan>? {
        var found: Member? = null
        var fClan: Clan? = null

        outer@ for (clan in Clan.clans) {
            if (clan.owner.id == id) {
                found = clan.owner
                fClan = clan
                break@outer
            }
            for (member in clan.members) {
                if (member.id == id) {
                    found = member
                    fClan = clan
                    break@outer
                }
            }
        }

        return if (found == null) null else found to fClan!!
    }

    fun findPlayer(id: UUID): Member? {
        return findPlayerAndClan(id)?.first
    }
    fun checkPlayer(uuid: UUID): Boolean = findPlayerAndClan(uuid) != null

    fun getClan(name: String) = Clan.clans.firstOrNull { it.name.equals(name, true) }
    fun getClanByPlayer(player: OfflinePlayer): Clan? {
        return findPlayerAndClan(player.uniqueId)?.second
    }

    fun isPrefixNotNull(clan: Clan) = clan.prefix != null
    fun prefixExists(prefix: Prefix) = Clan.clans.firstOrNull { it.prefix == prefix } != null

    fun getPrefixText(clan: Clan): String {

        return if (ClanUtilities.isPrefixNotNull(clan)) {
            clan.prefix!!.color.toString() + clan.prefix!!.text
        } else {
            "&cНет"
        }

    }
    fun save() = DataLoader.save()
}

