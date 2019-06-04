package ru.ovrays.squareclans

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object VaultHandler {

    lateinit var econ: Economy
    lateinit var perms: Permission

    fun setup(instance: JavaPlugin) {
        if (!setupEconomy()) {
            Bukkit.getLogger().severe(String.format("Vault не установлен!"))
            Bukkit.getPluginManager().disablePlugin(instance)
            return
        }
        setupPerms()

    }

    private fun setupEconomy(): Boolean {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false
        }
        val rsp = Bukkit.getServicesManager().getRegistration(Economy::class.java) ?: return false
        econ = rsp.provider
        return true
    }

    private fun setupPerms() {
        val rsp = Bukkit.getServicesManager().getRegistration(Permission::class.java)
        perms = rsp.provider
    }
}
