package ru.ovrays.squareclans.data.dto

class DTOConfig {
    val clanCreateCost = 25000
    val clanDeleteReward = 15000
    val clanRegexPattern = "^[A-Za-z_-]"
    val clanMinLength = 3
    val clanMaxLength = 17
    val clanIsInvalid = "�������� ����� ������ ���� ������ �� 3 �� 17 �������� � ��������� ������ ��������� ��������� � �������� ����� � ������� _ � -"

    val prefixChangeCost = 5000
    val prefixDeleteReward = 3000
    val prefixRegexPattern = "[^\\s][A-Z]"
    val prefixMinLength = 3
    val prefixMaxLength = 15
    val prefixIsInvalid = "�������� �������. ����� �������� ������ ���� ������ �� 3 �� 15 ��������� ��������� ��������, � ��� ���� �� ������ �������� �������������."

    val clanChatTag = "&9[&bClanChat&9]"

    val inviteMessage = "&a����������� � ���� &f%clan%&a. ������� &f/c invite accept&a ��� �������� �����������. ��� ����� ����� ������������� ��������� ����� 60 ������."
    val transferMessage = "&a����� ������ ����� &f%sender%&a �������� ��� ����������� �� �������� ���������. ������� &f/c transfer accept&a ��� �������� �����������. ��� ����� ������������� ��������� ����� 60 ������."
}