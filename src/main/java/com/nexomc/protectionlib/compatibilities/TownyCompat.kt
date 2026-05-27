package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import com.palmergames.bukkit.towny.`object`.TownyPermission
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class TownyCompat(mainPlugin: JavaPlugin, plugin: Plugin) : ProtectionCompatibility<Plugin>(mainPlugin, plugin) {
    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        return PlayerCacheUtil.getCachePermission(
            player, target, target.block.type,
            TownyPermission.ActionType.BUILD
        )
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        return PlayerCacheUtil.getCachePermission(
            player, target, target.block.type,
            TownyPermission.ActionType.DESTROY
        )
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        return PlayerCacheUtil.getCachePermission(
            player, target, target.block.type,
            TownyPermission.ActionType.SWITCH
        )
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        return PlayerCacheUtil.getCachePermission(
            player, target, target.block.type,
            TownyPermission.ActionType.ITEM_USE
        )
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        return PlayerCacheUtil.getCachePermission(
            player,
            entity.location,
            entity.location.block.type,
            TownyPermission.ActionType.DESTROY
        )
    }
}
