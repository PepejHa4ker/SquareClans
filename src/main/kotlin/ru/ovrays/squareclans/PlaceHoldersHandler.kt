package ru.ovrays.squareclans

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


object PlaceHoldersHandler {

    fun setup(instance: JavaPlugin) {
        if (!setupApi()) {
            Bukkit.getLogger().severe(String.format("PlaceholderAPI не установлен!"))
            Bukkit.getPluginManager().disablePlugin(instance)
            return
        } else {
            PlaceHolderExpansion().register()
        }
    }

    private fun setupApi(): Boolean {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
    }

}
