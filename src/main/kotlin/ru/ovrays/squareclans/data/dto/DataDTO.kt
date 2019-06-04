package ru.ovrays.squareclans.data.dto

import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.model.*
import java.util.*

object  DataDTO {
    class DTOClanList {
        var clans: Array<DTOClan>? = null
    }

    class DTOClan {
        var owner: DTOOwner? = null
        var name: String? = null
        var members: Array<DTOMember>? = null
        var prefix: DTOPrefix? = null
        var level: Int = Int.MIN_VALUE
        var points: Int = Int.MIN_VALUE
    }

    class DTOOwner {
        var uuid: String? = null
        var points: Int = 0
        var clanChat: Boolean = false
    }

    class DTOMember {
        var uuid: String? = null
        var rank: String? = null
        var points: Int = 0
        var clanChat: Boolean = false
    }

    class DTOPrefix {
        var color: String? = null
        var text: String? = null
    }

    object Converter {
        private fun assert(value: Boolean, message: String) {
            if (!value) {
                throw RuntimeException("Invalid Clan DTO: $message")
            }
        }

        private fun clanDTOToClan(clan: DTOClan): Clan? {
            try {
                try {
                    assert(
                        clan.level != Int.MIN_VALUE,
                        "level = MIN_VALUE"
                    )
                    assert(
                        clan.points != Int.MIN_VALUE,
                        "points = MIN_VALUE"
                    )
                } catch (e: RuntimeException) {
                    SquareClansPlugin.sendToConsole("Assertion error: $e")
                    return null
                }

                val clanObj = Clan(
                    name = clan.name!!,
                    owner = Member.Owner(UUID.fromString(clan.owner!!.uuid)),
                    prefix = clan.prefix?.let {
                        val level = ClanLevel.getLevel(clan.level)!!
                        Prefix(
                            SquareClansPlugin.color_strings[clan.prefix!!.color] ?: level.colors.shuffled()[0],
                            clan.prefix!!.text!!
                        )
                    },
                    level = ClanLevel.getLevel(clan.level)!!,
                    points = clan.points
                )
                clanObj.members.addAll(clan.members!!.distinctBy { it.uuid }.map { dto ->
                    Member(
                        UUID.fromString(dto.uuid),
                        Rank.valueOf(dto.rank!!),
                        dto.points,
                        dto.clanChat
                    )
                })
                return clanObj
            } catch (e: Exception) {
                SquareClansPlugin.sendToConsole("ERROR Loading clan: ${clan.name}")
                e.printStackTrace()
                return null
            }
        }

        fun convertFromDTO(clans: DTOClanList): HashSet<Clan> {
            val notNullClans = clans.clans!!.distinctBy { it.name }
            return notNullClans.mapNotNull(Converter::clanDTOToClan).distinctBy { it.name }.toHashSet()
        }

        fun convertToDTO(clans: List<Clan>): DTOClanList {

            return DTOClanList().apply {
                this.clans = clans.map { clan ->
                    DTOClan().apply {
                        this.level = clan.level.number
                        this.points = clan.points()
                        this.members = clan.members.map { member ->
                            DTOMember().apply {
                                this.uuid = member.id.toString()
                                this.rank = member.rank.name
                                this.points = member.points
                                this.clanChat = member.clanChat
                            }
                        }.toTypedArray()

                        this.name = clan.name
                        this.owner = DTOOwner().apply {
                            this.uuid = clan.owner.id.toString()
                            this.points = clan.owner.points
                            this.clanChat = clan.owner.clanChat
                        }

                        this.prefix = if (clan.prefix != null) {
                            DTOPrefix().apply {
                                this.color = SquareClansPlugin.colors[clan.prefix!!.color]!!
                                this.text = clan.prefix!!.text
                            }
                        } else null
                    }
                }.distinctBy { it.name + it.owner!!.uuid }.toTypedArray()
            }
        }
    }
}