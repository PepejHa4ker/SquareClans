package ru.ovrays.squareclans.api

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Member
import ru.ovrays.squareclans.util.ClanUtilities.findPlayerAndClan
import java.util.*

class SquareClansApi {
    val find: find = Companion.find
    val utils: utils = Companion.utils

    init {
        SquareClansApi = this
    }

    companion object  {
        lateinit var SquareClansApi: SquareClansApi

        object find {

            fun playerByClan(clan: Clan, id: UUID): Member? = clan.members.plus(clan.owner).find { it.id == id }
            fun clanByPlayer(player: OfflinePlayer): Clan? = findPlayerAndClan(player.uniqueId)?.second

            fun player(id: UUID): Member? = findPlayerAndClan(id)?.first
            fun clan(name: String) = Clan.clans.firstOrNull { it.name.equals(name, true) }
            fun maxMembers(clan: Clan) = (clan.members + clan.availableMembers())

            fun playerAndClan(id: UUID): Pair<Member, Clan>? {
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
        }

        object utils {
            fun isPlayerExists(uuid: UUID): Boolean = findPlayerAndClan(uuid) != null
            fun isPrefixExists(prefix: String) = Clan.clans.find { it.prefix!!.text.equals(prefix, true) } != null
            fun isPrefixNotNull(clan: Clan) = clan.prefix != null

            open class SquarePlayer(player: Player) : ru.ovrays.squareclans.util.ClanPlayer(player)

        }

    }
}


