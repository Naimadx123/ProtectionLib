package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import net.crashcraft.crashclaim.CrashClaim
import net.crashcraft.crashclaim.api.CrashClaimAPI
import net.crashcraft.crashclaim.permissions.PermissionRoute
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class CrashClaimCompat(mainPlugin: JavaPlugin, plugin: CrashClaim) : ProtectionCompatibility<CrashClaim>(mainPlugin, plugin) {
    val crashClaim: CrashClaimAPI = CrashClaimAPI(plugin)

    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        return crashClaim.permissionHelper.bypassManager.isBypass(player.uniqueId) ||
                crashClaim.getClaim(target)?.hasPermission(player.uniqueId, target, PermissionRoute.BUILD) != false
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        return crashClaim.permissionHelper.bypassManager.isBypass(player.uniqueId) ||
                crashClaim.getClaim(target)?.hasPermission(player.uniqueId, target, PermissionRoute.BUILD) != false
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        return crashClaim.permissionHelper.bypassManager.isBypass(player.uniqueId) ||
                crashClaim.getClaim(target)?.hasPermission(player.uniqueId, target, PermissionRoute.INTERACTIONS) != false
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        return crashClaim.permissionHelper.bypassManager.isBypass(player.uniqueId) ||
                crashClaim.getClaim(target)?.hasPermission(player.uniqueId, target, PermissionRoute.INTERACTIONS) != false
    }

    /**
     * @param player Player looking to use an item
     * @param entity Entity that players trying to damage
     * @return true if he can use the item at the location
     */
    override fun canDamage(player: Player, entity: Entity): Boolean {
        return crashClaim.permissionHelper.bypassManager.isBypass(player.uniqueId) ||
                crashClaim.getClaim(entity.location)?.hasPermission(player.uniqueId, entity.location, PermissionRoute.ENTITIES) != false
    }

}
