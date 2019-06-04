package ru.ovrays.squareclans.model

import ru.ovrays.squareclans.util.ColorUtilities

enum class Permission(val description: String) {
    DELETE("������� ����"),
    MODIFY("�������� ��������, �������"),
    INVITE_EXPEL_MEMBERS("��������� � �������� ����������"),
    MODIFY_MEMBERS("�������� ����� ����������"),
    CLAN_CHAT_WRITE("������ � ��� �����"),
    CLAN_CHAT_RECEIVE("������ ��� �����");

    fun getText() : String {
        return ColorUtilities.GREEN+this.name.toUpperCase() + ColorUtilities.WHITE+ " ($description)"
    }
}