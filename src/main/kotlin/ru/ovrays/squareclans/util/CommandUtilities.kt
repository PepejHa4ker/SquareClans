package ru.ovrays.squareclans.util

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import ru.ovrays.squareclans.command.wrapper.CommandError
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Rank

object CommandUtilities {
    fun getPlayer(name: String): OfflinePlayer {
        return Bukkit.getServer().onlinePlayers.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } ?: throw CommandError("Игрок не найден: $name", false)
    }

    fun getRank(int: Int): Rank {
        return Rank.values()
            .first {
                it.priority == int
            }
    }

    fun isRankMax(rank: Rank): Boolean {
        return rank.priority == 2
    }

    fun isRankMin(rank: Rank): Boolean {
        return rank.priority == 1
    }

    fun getRank(rankRaw: String): Rank {
        return Rank.values()
            .firstOrNull {
                it.name.equals(rankRaw, ignoreCase = true) or it.localizedName.equals(
                    rankRaw,
                    ignoreCase = true
                )
            }
            ?: throw CommandError("Ранк не найден: $rankRaw", false)
    }

    fun getClan(name: String): Clan {
        return ClanUtilities.getClan(name) ?: throw CommandError("Клан не найден: $name", false)
    }
}