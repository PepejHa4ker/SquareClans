package ru.ovrays.squareclans.util

import org.bukkit.ChatColor
import ru.ovrays.squareclans.SquareClansPlugin

object ColorUtilities {
    val GREEN = ChatColor.GREEN.toString()
    val AQUA = ChatColor.AQUA.toString()
    val WHITE = ChatColor.WHITE.toString()
    val RESET = ChatColor.RESET.toString()
    val RED = ChatColor.RED.toString()
    val YELLOW = ChatColor.YELLOW.toString()
    val BLUE = ChatColor.BLUE.toString()
    val GOLD = ChatColor.GOLD.toString()
    val DARK_PURPLE = ChatColor.DARK_PURPLE.toString()
    val GRAY = ChatColor.GRAY.toString()
    val DARK_GRAY = ChatColor.DARK_GRAY.toString()
    val STRIKED_OUT = ChatColor.STRIKETHROUGH

    enum class Level(var colorArray: List<ChatColor>) {
        A(arrayListOf(ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.WHITE)),
        B(arrayListOf(ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.GOLD)),
        C(arrayListOf(ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE)),
        D(arrayListOf(ChatColor.BLUE, ChatColor.DARK_BLUE, ChatColor.DARK_AQUA)),
        E(arrayListOf(ChatColor.DARK_RED, ChatColor.BLACK, ChatColor.AQUA)),
        AB(put(Level.A, Level.B)),
        ABC(put(Level.AB, Level.C)),
        ABCD(put(Level.ABC, Level.D)),
        ABCDE(put(Level.ABCD, Level.E));

    }

    fun put(a: Level, b: Level): List<ChatColor> {
        return a.colorArray.plus(b.colorArray)
    }

    fun putToMap(color: ChatColor) {
        SquareClansPlugin.colors[color] = color.name.toLowerCase()
        SquareClansPlugin.color_strings[color.name.toLowerCase()] = color
    }

    fun putDyeToMap() {

        fun put(int: Int, chatColor: ArrayList<ChatColor>) { SquareClansPlugin.color_dyes[chatColor] = int }

        put(7, arrayListOf(ChatColor.GRAY))
        put(8, arrayListOf(ChatColor.DARK_GRAY))
        put(15, arrayListOf(ChatColor.WHITE))
        put(13, arrayListOf(ChatColor.LIGHT_PURPLE))
        put(11, arrayListOf(ChatColor.YELLOW))
        put(14, arrayListOf(ChatColor.GOLD))
        put(10, arrayListOf(ChatColor.GREEN))
        put(2, arrayListOf(ChatColor.DARK_GREEN))
        put(5, arrayListOf(ChatColor.DARK_PURPLE))
        put(12, arrayListOf(ChatColor.BLUE))
        put(4, arrayListOf(ChatColor.DARK_BLUE))
        put(6, arrayListOf(ChatColor.DARK_AQUA, ChatColor.AQUA))
        put(6, arrayListOf(ChatColor.DARK_RED))
        put(0, arrayListOf(ChatColor.BLACK))

    }

}