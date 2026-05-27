package com.nexomc.protectionlib

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

abstract class ProtectionCompatibility<T : Plugin>(val mainPlugin: JavaPlugin, val plugin: T) {
    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    abstract fun canBuild(player: Player, target: Location): Boolean

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    abstract fun canBreak(player: Player, target: Location): Boolean

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    abstract fun canInteract(player: Player, target: Location): Boolean

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    abstract fun canUse(player: Player, target: Location): Boolean

    /**
     * @param player Player looking to use an item
     * @param entity Entity that players trying to damage
     * @return true if he can use the item at the location
     */
    abstract fun canDamage(player: Player, entity: Entity): Boolean
}
