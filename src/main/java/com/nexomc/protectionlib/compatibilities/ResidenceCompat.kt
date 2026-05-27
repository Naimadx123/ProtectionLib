package com.nexomc.protectionlib.compatibilities

import com.bekvon.bukkit.residence.Residence
import com.bekvon.bukkit.residence.containers.Flags
import com.bekvon.bukkit.residence.protection.ClaimedResidence
import com.nexomc.protectionlib.ProtectionCompatibility
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class ResidenceCompat(mainPlugin: JavaPlugin, plugin: Residence) : ProtectionCompatibility<Residence>(mainPlugin, plugin) {

    private fun canDo(player: Player, target: Location, flag: Flags?): Boolean {
        return player.isOp || plugin.residenceManager.getByLoc(target)?.permissions?.playerHas(player, flag, false) != false
    }

    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        return canDo(player, target, Flags.build)
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        return canDo(player, target, Flags.destroy)
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        // No single interact flag, so just check if player is on their own island
        return canDo(player, target, Flags.use)
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        // No single use flag, so just check if player is on their own island
        return canDo(player, target, Flags.use)
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        val flag = if (entity is Player) {
            Flags.pvp
        } else {
            Flags.damage
        }

        return canDo(player, entity.location, flag)
    }
}
