package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import me.angeschossen.lands.api.LandsIntegration
import me.angeschossen.lands.api.flags.type.Flags
import me.angeschossen.lands.api.flags.type.RoleFlag
import me.angeschossen.lands.api.land.LandWorld
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class LandsCompat(mainPlugin: JavaPlugin, plugin: Plugin) : ProtectionCompatibility<Plugin>(mainPlugin, plugin) {
    val landsIntegration = LandsIntegration.of(mainPlugin)

    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        return hasFlag(target, player, Flags.BLOCK_PLACE)
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        return hasFlag(target, player, Flags.BLOCK_BREAK)
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        return hasFlag(target, player, Flags.INTERACT_GENERAL)
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        return hasFlag(target, player, Flags.INTERACT_GENERAL)
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        val flag = when (entity) {
            is Player -> Flags.ATTACK_PLAYER
            is Monster -> Flags.ATTACK_MONSTER
            else -> Flags.ATTACK_ANIMAL
        }

        return hasFlag(entity.location, player, flag)
    }

    /**
     * Checks if a player's role has a flag at the given position.
     * This does check bypass perms and wilderness flags (/lands admin menu) as well.
     *
     * @param location Location of interaction
     * @param player   Player that seeks to do stuff
     * @param flag     The Lands flag
     * @return false if not allowed
     */
    private fun hasFlag(location: Location, player: Player, flag: RoleFlag): Boolean {
        val landWorld = landsIntegration.getWorld(location.world) ?: return true
        return landWorld.hasRoleFlag(landsIntegration.getLandPlayer(player.uniqueId), location, flag, null, true)
    }
}