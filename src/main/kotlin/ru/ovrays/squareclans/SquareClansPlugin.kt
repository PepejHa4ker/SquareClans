package ru.ovrays.squareclans

import com.gmail.filoghost.holographicdisplays.api.Hologram
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import ru.ovrays.squareclans.api.SquareClansApi
import ru.ovrays.squareclans.command.CommandClan
import ru.ovrays.squareclans.command.CommandClanChat
import ru.ovrays.squareclans.command.TabComplete
import ru.ovrays.squareclans.command.wrapper.CommandDispatcher
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.event.EventListener
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.ClanLevel
import ru.ovrays.squareclans.util.ClanPlayer
import ru.ovrays.squareclans.util.ColorUtilities
import ru.ovrays.squareclans.util.ImmutableGui
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class SquareClansPlugin : JavaPlugin() {

    init {
        instance = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(EventListener(), this)
        server.pluginManager.registerEvents(ImmutableGui, this)

        VaultHandler.setup(this)
        PlaceHoldersHandler.setup(this)

        ChatColor.values().forEach {
            ColorUtilities.putToMap(it)
        }

        ColorUtilities.putDyeToMap()
        ClanLevel.loadLevels()

        DataLoader.load()
        ConfigLoader.getConfig()

        CommandDispatcher.register(CommandClanChat.build(), "cc")
        CommandDispatcher.register(CommandClan.build(), "c", "clan")

        getCommand("c").tabCompleter = TabComplete()
        getCommand("clan").tabCompleter = TabComplete()
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        return CommandDispatcher.onCommand(sender, label, args)
    }

    override fun onDisable() {
        SquareClansPlugin.sendToConsole("Saving clans...")
        DataLoader.save()
    }

    companion object {

        fun broadcastMessage(text: String) {
            Bukkit.getServer().broadcastMessage(text)
        }

        fun getApi(): SquareClansApi {
            return SquareClansApi()
        }

        @JvmStatic
        private var instance: SquareClansPlugin? = null

        fun sendToConsole(text: String) {
            this.pluginObject.logger.info(text)
        }

        fun sendToModerators(text: String, from: ClanPlayer) {
            val post = ChatColor.translateAlternateColorCodes('&', text)
            val pre = ChatColor.translateAlternateColorCodes('&', "&8[ClanSpy]&7[&c${from.getClan().name}&7] ${from.name}: ")
        }

        fun buildTask(delayTicks: Int, execute: () -> Unit) {
            Bukkit.getScheduler()
                .scheduleSyncDelayedTask(pluginObject, { execute() }, delayTicks.toLong())
        }

        val pluginObject : SquareClansPlugin
            get() = instance!!

        val invites: HashMap<UUID, UUID> = HashMap()
        val clan_delete_queue: HashMap<UUID, Clan> = HashMap()
        val leader_transfer_queue: HashMap<UUID, UUID> = HashMap()

        internal val colors: HashMap<ChatColor, String> = HashMap()
        internal val color_strings: HashMap<String, ChatColor> = HashMap()
        internal val color_dyes: HashMap<ArrayList<ChatColor>, Int> = HashMap()

        val config_dir = File("plugins/SquareClans/")
    }
}

