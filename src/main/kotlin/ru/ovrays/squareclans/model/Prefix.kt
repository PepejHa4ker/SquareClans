package ru.ovrays.squareclans.model

import org.bukkit.ChatColor
import ru.ovrays.squareclans.data.file.ConfigLoader


data class Prefix(var color: ChatColor = ChatColor.WHITE, var text: String) {
    companion object {
        private var pattern = Regex(ConfigLoader.getConfig().prefixRegexPattern + "{${ConfigLoader.getConfig().prefixMinLength},${ConfigLoader.getConfig().prefixMaxLength}}")
    }

    init {
        pattern = Regex(ConfigLoader.getConfig().prefixRegexPattern + "{${ConfigLoader.getConfig().prefixMinLength},${ConfigLoader.getConfig().prefixMaxLength}}")
    }

    var isValid: Boolean = text.matches(pattern) && color.isColor
}