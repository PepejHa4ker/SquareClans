package ru.ovrays.squareclans.api

import ru.ovrays.squareclans.event.AbstractEvent
import ru.ovrays.squareclans.model.Member

class ClanAddMemberEvent(var member: Member) : AbstractEvent
