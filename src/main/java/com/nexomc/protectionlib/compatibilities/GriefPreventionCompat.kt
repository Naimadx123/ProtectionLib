package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import me.ryanhamshire.GriefPrevention.Claim
import me.ryanhamshire.GriefPrevention.ClaimPermission
import me.ryanhamshire.GriefPrevention.GriefPrevention
import me.ryanhamshire.GriefPrevention.PlayerData
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class GriefPreventionCompat(mainPlugin: JavaPlugin, plugin: GriefPrevention) : ProtectionCompatibility<GriefPrevention>(mainPlugin, plugin) {
    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        return checkPermission(player, target, ClaimPermission.Build)
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        return checkPermission(player, target, ClaimPermission.Build)
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        return checkPermission(player, target, ClaimPermission.Access)
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        return checkPermission(player, target, ClaimPermission.Access)
    }

    /**
     * @param player Player trying to damage an entity
     * @param entity Entity that a player is trying to damage
     * @return true if he can damage the entity
     */
    override fun canDamage(player: Player, entity: Entity): Boolean {
        return when (entity) {
            is Player -> checkPermission(player, entity.location, ClaimPermission.Inventory)
            else -> checkPermission(player, entity.location, ClaimPermission.Build)
        }
    }

    private fun checkPermission(player: Player, target: Location, permission: ClaimPermission): Boolean {
        val playerData: PlayerData = plugin.dataStore.getPlayerData(player.uniqueId).takeUnless { it.ignoreClaims } ?: return true
        val claim = plugin.dataStore.getClaimAt(target, false, playerData.lastClaim) ?: return true

        playerData.lastClaim = claim
        return claim.checkPermission(player, permission, null) == null
    }
}
