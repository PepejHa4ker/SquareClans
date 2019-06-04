package ru.ovrays.squareclans.api

import ru.ovrays.squareclans.event.AbstractEvent
import ru.ovrays.squareclans.model.ClanLevel

class ClanLevelUpEvent(var before: ClanLevel, var after: ClanLevel) : AbstractEvent