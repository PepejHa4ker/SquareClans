package ru.ovrays.squareclans.event

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.api.ClanExperienceGainEvent
import ru.ovrays.squareclans.api.ClanExperienceSetEvent
import ru.ovrays.squareclans.api.EventBus
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.ClanLevel
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ClanPlayer


class EventListener: Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(e: AsyncPlayerChatEvent) {

        val cp = try { ClanPlayer(e.player) } catch (e: Exception) { return }
        val c = try { cp.getClan() } catch (e: Exception) { return }

        if (cp.isInClan()) {
            if(cp.hasClanPermission(Permission.CLAN_CHAT_RECEIVE) && cp.hasPermission("SquareClans.command.chat") && cp.getMember().clanChat)  {
                c.broadcastToMembers("${ConfigLoader.getConfig().clanChatTag} ${cp.displayName}&r: ${e.message}")
                SquareClansPlugin.sendToConsole("[ClanChat][${cp.getClan().name}] ${cp.displayName}: ${e.message}")
                SquareClansPlugin.sendToModerators(e.message, cp)
                e.isCancelled = true
            }
            if(c.level.number >= 3 && c.prefix != null) {
                val color = c.prefix!!.color.toString()
                e.format = "${ChatColor.DARK_GRAY}[$color${c.prefix!!.text}${ChatColor.DARK_GRAY}]${ChatColor.RESET} " + e.format
            }
        }
    }

    @EventHandler
    fun onPlayerKill(e: PlayerDeathEvent) {
        e.entity?: return
        val why = ClanPlayer(e.entity.killer?: return)

        if (!why.isInClan()) return
        why.getMember().addPoints(2)
    }

    init {
        EventBus.listen<ClanExperienceGainEvent> {
            val p = it.clan

            try {
                if (p.points() >= ClanLevel.getLevel(p.level.number + 1)!!.experience) {
                    p.updateLevel(p.level.number + 1)
                }
            } catch (e: Exception) {}

            DataLoader.save()
        }

        EventBus.listen<ClanExperienceSetEvent> {
            val p = it.clan

            try {
                if (p.points() >= ClanLevel.getLevel(p.level.number + 1)!!.experience) {
                    p.updateLevel(p.level.number + 1)
                }
            } catch(e: Exception) {}

            DataLoader.save()   
        }

    }

}