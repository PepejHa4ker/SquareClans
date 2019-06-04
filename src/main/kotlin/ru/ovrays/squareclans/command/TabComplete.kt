package ru.ovrays.squareclans.command

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.util.ClanPlayer

class TabComplete: TabCompleter {   

    fun getSubCommandsAliases(): ArrayList<String> {
        val subcommands = arrayListOf<String>()

        CommandClan.subcommands.forEach {
            subcommands.add(it.aliases.first())
        }

        return subcommands
    }

    fun getOnlinePlayers(): MutableList<String> {
        val players  = mutableListOf<String>()
        Bukkit.getServer().onlinePlayers.forEach {
            players.add(it.name)
        }
        return players
    }

    override fun onTabComplete(
        sender: CommandSender?,
        command: Command?,
        alias: String?,
        args: Array<out String>?
    ): List<String>? {
        try {
            when(args!![0]) {

                "delete" -> {
                    return args[1].getSubCommandsByCharacters(mutableListOf("accept", "deny"))
                }

                "invite"  -> {
                    return args[1].getSubCommandsByCharacters(mutableListOf("accept", "deny").plus(getOnlinePlayers()).toMutableList())
                }

                "transfer" -> {
                    return args[1].getSubCommandsByCharacters(mutableListOf("accept", "deny").plus(getOnlinePlayers()).toMutableList())
                }

                in arrayListOf("kick", "expel") -> {
                    return args[1].getSubCommandsByCharacters(getOnlinePlayers())
                }

                "rank" -> {
                    if (args[1] == "set") {
                        if (args[2].isEmpty()) {
                            return args[2].getSubCommandsByCharacters(getOnlinePlayers())
                        } else if (args[2] in getOnlinePlayers()) {
                            return args[3].getSubCommandsByCharacters(mutableListOf("member", "officer", "owner"))
                        }
                    } else if(args[1] in arrayListOf("up", "down"))  {
                        args[2].getSubCommandsByCharacters(getOnlinePlayers())
                    } else {
                        args[1].getSubCommandsByCharacters(mutableListOf("set", "up", "down"))
                    }
                }

                "points" -> {
                    if(args[1] == "add" || args[1] == "remove") {
                        args[2].getSubCommandsByCharacters(getOnlinePlayers())
                    } else {
                        args[1].getSubCommandsByCharacters(mutableListOf("add", "remove"))
                    }
                }

                "prefix" -> {
                    if(args[1] == "set") {
                        if (args[2] in SquareClansPlugin.color_strings.keys) {
                            return null
                        } else {
                            val p = ClanPlayer(Bukkit.getPlayer(sender!!.name))

                            val colors = SquareClansPlugin.color_strings.values
                                .filter { it in p.getClan().level.colors }
                                .mapNotNull { SquareClansPlugin.colors[it] }

                            return args[3].getSubCommandsByCharacters(colors.toMutableList())
                        }
                    } else if(args[1] == "unset") {
                        return null
                    }
                }

                else -> return args[0].getSubCommandsByCharacters()!!.toMutableList()
            }

        } catch (e: Exception) {}
        return null
    }

 private fun String.getSubCommandsByCharacters(within: MutableList<String>? = null): List<String>? {
        val aliases = within ?: getSubCommandsAliases()
        when (this.length) {

            0 -> return aliases
            1 -> return aliases.filter { it.startsWith(this[0], true) }
            2 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) }
            3 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) && it[2].equals(this[2], true) }
            4 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) && it[2].equals(this[2], true) && it[3].equals(this[3], true) }
            5 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) && it[2].equals(this[2], true) && it[3].equals(this[3], true) && it[4].equals(this[4], true) }
            6 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) && it[2].equals(this[2], true) && it[3].equals(this[3], true) && it[4].equals(this[4], true) && it[5].equals(this[5], true) }
            7 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) && it[2].equals(this[2], true) && it[3].equals(this[3], true) && it[4].equals(this[4], true) && it[5].equals(this[5], true) && it[6].equals(this[6], true) }
            8 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) && it[2].equals(this[2], true) && it[3].equals(this[3], true) && it[4].equals(this[4], true) && it[5].equals(this[5], true) && it[6].equals(this[6], true) && it[7].equals(this[7], true) }
            9 -> return aliases.filter { it.startsWith(this[0], true) && it[1].equals(this[1], true) && it[2].equals(this[2], true) && it[3].equals(this[3], true) && it[4].equals(this[4], true) && it[5].equals(this[5], true) && it[6].equals(this[6], true) && it[7].equals(this[7], true) && it[8].equals(this[8], true) }
        }
        return null
    }

}