package ru.ovrays.squareclans

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import ru.ovrays.squareclans.SquareClansPlugin.Companion.sendToConsole
import ru.ovrays.squareclans.util.ClanPlayer
import ru.ovrays.squareclans.util.ClanUtilities.getTop
import ru.ovrays.squareclans.util.ColorUtilities

class PlaceHolderExpansion : PlaceholderExpansion() {

    override fun canRegister(): Boolean {
        return true
    }

    override fun getAuthor(): String {
        return "ovrays"
    }

    override fun getIdentifier(): String {
        return "squareclans"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {

        val nameRegex = Regex("^top([1-9]|10)$")
        val tagRegex = Regex("^top([1-9]|10)_tag")
        val levelRegex = Regex("^top([1-9]|10)_level")
        val expRegex = Regex("^top([1-9]|10)_exp")

        val pNameRegex = Regex("^clan$")
        val pTagRegex = Regex("^tag$")

        when {

            identifier.matches(pNameRegex) -> {

                return try {
                    ClanPlayer(player as Player).getClan().name
                } catch (e: Exception) {
                    ""
                }

            }

            identifier.matches(pTagRegex) -> {
                return try {
                    val clan =ClanPlayer(player as Player).getClan()

                    if (clan.prefix != null) {
                        ColorUtilities.DARK_GRAY + "[" + clan.prefix!!.color.toString() + clan.prefix!!.text + ColorUtilities.DARK_GRAY + "] "
                    } else {
                        ""
                    }
                } catch (e: Exception) {
                    ""
                }

            }

            identifier.matches(nameRegex) -> {
               val pos = identifier.last().toString().toInt()
                val clan = getTop(pos)

                return clan?.name ?: "&c&m-"
            }

            identifier.matches(tagRegex) -> {
                val pos = identifier.removeSuffix("_tag").last().toString().toInt()
                val clan = getTop(pos) ?: return ""

                return if (clan.prefix != null) {
                    clan.prefix!!.color.toString() + clan.prefix!!.text
                } else {
                    null
                }
            }

            identifier.matches(levelRegex) -> {
                val pos = identifier.removeSuffix("_level").last().toString().toInt()
                val clan = getTop(pos) ?: return ""

                return clan.level.number.toString()
            }

            identifier.matches(expRegex) -> {
                val pos = identifier.removeSuffix("_exp").last().toString().toInt()
                val clan = getTop(pos) ?: return ""

                return clan.points().toString()
            }

        }

        return null
    }
}















/*val regex = Regex("^top([1-9]|10)$")

return if(identifier.matches(regex)) {

    val position = identifier.last().toInt()
    val clan = getTop(position)

    if(clan == null) {
        "             &c&m--"
    } else {
        SquareClansPlugin.sendToConsole("Requested placeholder:\n\"&c$position. &a${clan.name} &8[&c${clan.level.number}&8 level  - &c${clan.points()} &8points]\\n\"\n")
        "&c$position. &a${clan.name} &8[&c${clan.level.number}&8 level  - &c${clan.points()} &8points]\n"
    }

} else null
*/
