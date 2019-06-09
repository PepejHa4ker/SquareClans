package ru.ovrays.squareclans.model

import ru.ovrays.squareclans.util.ColorUtilities

enum class Rank(
    val priority: Int,
    val rank: String,
    val localizedName: String,
    val description: String,
    private val perms: Set<Permission>
) {
    OWNER(
        3,
        "owner",
        "Лидер",
        "Лидер клана. имеет все права.",
        Permission.values().toSet()
    ),
    OFFICER(
        2,
        "officer",
        "Офицер",
        "Офицер клана. имеет все права владельца, кроме удаления клана и изменени¤ названия/префикса.",
        OWNER.perms.minus(arrayOf(Permission.MODIFY, Permission.DELETE))
    ),
    MEMBER(
        1,
        "member",
        "Участник",
        "Участник клана. имеет стандартные права.",
        OFFICER.perms.minus(arrayOf(Permission.INVITE_EXPEL_MEMBERS, Permission.MODIFY_MEMBERS))
    );

    fun hasPermission(permission: Permission) = this.perms.contains(permission)
    fun canModify(other: Rank): Boolean {
        return this.priority > other.priority
    }

    fun getInfo(): String {
        val builder = StringBuilder()
        builder.append(ColorUtilities.RED + "описание:\n")
        this.description.split("\n").forEach { builder.append(ColorUtilities.YELLOW + it).append("\n") }
        builder.append(ColorUtilities.RED + "права:\n")
        perms.sortedByDescending { priority }.forEach {
            builder.append(ColorUtilities.AQUA + " - " + ColorUtilities.RESET + it.getText() + "\n")
        }
        return builder.toString()
    }

    override fun toString() = this.rank
}
