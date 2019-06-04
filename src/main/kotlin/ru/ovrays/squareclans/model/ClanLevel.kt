package ru.ovrays.squareclans.model

import org.bukkit.ChatColor
import ru.ovrays.squareclans.util.ColorUtilities.Level

class  ClanLevel constructor(val number: Int, val members: Int, val experience: Int,
                            var newColors: List<ChatColor> = arrayListOf(), var colors: List<ChatColor> = arrayListOf()) {

    override fun toString(): String {
        return "ClanLevel@[number=$number,experience:$experience]"
    }

    companion object {
        fun getLevelForColor(color: ChatColor) : Int {
            return clanLevelHashMap.values.first { color in it.newColors }.number
        }
        fun getLevel(level: Int): ClanLevel? = clanLevelHashMap[level]
        val First_Level = ClanLevel(1, 10, 100)
        private var clanLevelHashMap: HashMap<Int, ClanLevel> = hashMapOf(First_Level.number to First_Level)

        val clanLevels = ArrayList<ClanLevel>()


        fun loadLevels() {
            for (i in 2..20) {
                clanLevels.add(ClanLevel(i, (i + 10).let { it - it % 2 }, (i + 4).let { it - it % 2 } * 50))
            }

            clanLevels.remove(ClanLevel(3, 12, 300, arrayListOf(), arrayListOf()))
            clanLevels.remove(ClanLevel(5, 14, 400, arrayListOf(), arrayListOf()))
            clanLevels.remove(ClanLevel(10, 20, 700, arrayListOf(), arrayListOf()))
            clanLevels.remove(ClanLevel(15, 24, 900, arrayListOf(), arrayListOf()))
            clanLevels.remove(ClanLevel(20, 30, 1200, arrayListOf(), arrayListOf()))

            clanLevels.add(ClanLevel(3, 12, 300, Level.A.colorArray, Level.A.colorArray))
            clanLevels.add(ClanLevel(5, 14, 400, Level.B.colorArray, Level.AB.colorArray))
            clanLevels.add(ClanLevel(10, 20, 700, Level.C.colorArray, Level.ABC.colorArray))
            clanLevels.add(ClanLevel(15, 24, 900, Level.D.colorArray, Level.ABCD.colorArray))
            clanLevels.add(ClanLevel(20, 30, 1200, Level.E.colorArray, Level.ABCDE.colorArray))
            clanLevels.add(ClanLevel(21, 30, 1000000, arrayListOf(), arrayListOf()))

            clanLevels.forEach { level -> clanLevelHashMap[level.number] = level}
        }
    }
}
