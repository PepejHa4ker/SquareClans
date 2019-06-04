package ru.ovrays.squareclans.data.dto

class DTOConfig {
    val clanCreateCost = 25000
    val clanDeleteReward = 15000
    val clanRegexPattern = "^[A-Za-z_-]"
    val clanMinLength = 3
    val clanMaxLength = 17
    val clanIsInvalid = "Название клана должно быть длиной от 3 до 17 символов и содержать только латинские прописные и строчные буквы и символы _ и -"

    val prefixChangeCost = 5000
    val prefixDeleteReward = 3000
    val prefixRegexPattern = "[^\\s][A-Z]"
    val prefixMinLength = 3
    val prefixMaxLength = 15
    val prefixIsInvalid = "Неверный префикс. Текст префикса должен быть длиной от 3 до 15 прописных латинских символов, а его цвет не должен являться форматирующим."

    val clanChatTag = "&9[&bClanChat&9]"

    val inviteMessage = "&aПриглашение в клан &f%clan%&a. Введите &f/c invite accept&a для принятия приглашения. Оно будет будет автоматически отклонено через 60 секунд."
    val transferMessage = "&aЛидер вашего клана &f%sender%&a отправил вам предложение на передачу лидерства. Введите &f/c transfer accept&a для принятия предложения. Оно будет автоматически отклонено через 60 секунд."
}