package ru.ovrays.squareclans.api

import ru.ovrays.squareclans.event.AbstractEvent
import ru.ovrays.squareclans.model.Member

class ClanRemoveMemberEvent(var member: Member) : AbstractEvent
