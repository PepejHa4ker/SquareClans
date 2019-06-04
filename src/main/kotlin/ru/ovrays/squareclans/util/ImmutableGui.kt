@file:Suppress("DEPRECATION")

package ru.ovrays.squareclans.util

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta


class ImmutableGui(rows: Int, name: String) {
    open class Slot(
        private val material: Material,
        private val name: String? = null,
        private val itemLore: Collection<String> = listOf(),
        private val amount: Int = 1,
        private val data: Byte? = null
    ) {

        protected open fun parseItemStack(): ItemStack {

            val stack = if(data != null)
                ItemStack(material, amount, 0, data)
            else
                ItemStack(material, amount)

            stack.itemMeta = stack.itemMeta.apply {
                if (name != null)
                    this.displayName = ChatColor.translateAlternateColorCodes('&', name)
                if(itemLore.isNotEmpty())
                    this.lore = itemLore.toMutableList().map { ColorUtilities.RESET + it }
            }
            return stack
        }

        val itemStack = parseItemStack()

        companion object {
            private fun fromStack(stack: ItemStack): Slot = object : Slot(Material.AIR) {
                override fun parseItemStack(): ItemStack {
                    return stack
                }
            }

            fun fillerGenerator(name: String, lore: Collection<String>) = {
                Slot(
                    Material.STAINED_GLASS_PANE,
                    name,
                    lore.toMutableList(),
                    data = DyeColor.WHITE.dyeData
                )
            }

            fun head(itemName: String? = null, itemLore: Collection<String> = listOf(), owner: OfflinePlayer): Slot {
                val skull = ItemStack(Material.SKULL_ITEM, 1, 3)
                val skullMeta = skull.itemMeta as SkullMeta

                skull.itemMeta = skullMeta.apply {
                    if(itemName != null)
                        displayName = ChatColor.translateAlternateColorCodes('&', itemName)
                    if(itemLore.isNotEmpty())
                        lore = itemLore.toMutableList()
                    owningPlayer = owner
                }
                return fromStack(skull)
            }
        }
    }

    private val inv = Bukkit.createInventory(null, 9 * rows, ChatColor.translateAlternateColorCodes('&', name))
    private var pos = 0

    fun skip(count: Int, name: String = ColorUtilities.WHITE + " ", lore: Collection<String> = listOf()): ImmutableGui =
        fill(count, Slot.fillerGenerator(name, lore))

    fun fill(count: Int, creator: () -> Slot): ImmutableGui {
        add(*Array(count) { creator() })

        return this
    }

    fun skipToEnd(): ImmutableGui = skip(9 - pos.rem(9))

    fun add(vararg items: Slot): ImmutableGui = add(items.toList())

    fun add(items: Collection<Slot>): ImmutableGui {
        items.map {
            it.itemStack
        }.toTypedArray().forEach {
            inv.setItem(pos++, it)
        }

        return this
    }

    fun show(player: Player) {
        listened += inv.hashCode()
        player.openInventory(inv)
    }


    companion object : Listener {

        private val listened = hashSetOf<Int>()

        @EventHandler
        fun onInventoryClick(event: InventoryClickEvent) {
            val inventory = event.inventory
            val hash = inventory.hashCode()
            if (hash in listened) {
                event.isCancelled = true
            }
        }
    }
}