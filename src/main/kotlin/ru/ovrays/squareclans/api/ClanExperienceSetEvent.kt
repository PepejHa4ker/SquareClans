package ru.ovrays.squareclans.api

import org.bukkit.entity.Player
import ru.ovrays.squareclans.event.AbstractEvent
import ru.ovrays.squareclans.model.Clan

class ClanExperienceSetEvent(var amount: Int,  var playerCause: Player?, var clan: Clan) :
    AbstractEvent