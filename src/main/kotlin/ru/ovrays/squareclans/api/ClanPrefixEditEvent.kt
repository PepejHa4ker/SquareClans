package ru.ovrays.squareclans.api

import ru.ovrays.squareclans.event.AbstractEvent
import ru.ovrays.squareclans.model.Prefix

class ClanPrefixEditEvent(var before: Prefix?, var after: Prefix?) : AbstractEvent
