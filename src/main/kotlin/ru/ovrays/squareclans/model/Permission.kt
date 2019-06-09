package ru.ovrays.squareclans.model

import ru.ovrays.squareclans.util.ColorUtilities

enum class Permission(val description: String) {
    DELETE("Удалить клан"),
    MODIFY("Изменять название, префикс"),
    INVITE_EXPEL_MEMBERS("Принимать и выгонять участников"),
    MODIFY_MEMBERS("Изменять ранги участников"),
    CLAN_CHAT_WRITE("Писать в чат клана"),
    CLAN_CHAT_RECEIVE("Читать чат клана");

    fun getText() : String {
        return ColorUtilities.GREEN+this.name.toUpperCase() + ColorUtilities.WHITE+ " ($description)"
    }
}
