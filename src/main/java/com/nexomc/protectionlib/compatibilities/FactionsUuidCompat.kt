package com.nexomc.protectionlib.compatibilities

import com.massivecraft.factions.FactionsPlugin
import com.massivecraft.factions.listeners.FactionsBlockListener
import com.massivecraft.factions.listeners.FactionsEntityListener
import com.massivecraft.factions.listeners.FactionsPlayerListener
import com.massivecraft.factions.perms.PermissibleActions
import com.nexomc.protectionlib.ProtectionCompatibility
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class FactionsUuidCompat(mainPlugin: JavaPlugin, plugin: FactionsPlugin) : ProtectionCompatibility<FactionsPlugin>(mainPlugin, plugin) {

    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        return !plugin.worldUtil().isEnabled(target.world) || FactionsBlockListener.playerCanBuildDestroyBlock(player, target, PermissibleActions.BUILD, false)
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        return !plugin.worldUtil().isEnabled(target.world) || FactionsBlockListener.playerCanBuildDestroyBlock(player, target, PermissibleActions.DESTROY, false)
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        return !plugin.worldUtil().isEnabled(target.world) || FactionsPlayerListener.canUseBlock(player, target.block.type, target, true)
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        return !plugin.worldUtil().isEnabled(target.world) || FactionsPlayerListener.canInteractHere(player, target)
    }

    /**
     * @param player Player looking to use an item
     * @param entity Entity that players trying to damage
     * @return true if he can use the item at the location
     */
    override fun canDamage(player: Player, entity: Entity): Boolean {
        return !plugin.worldUtil().isEnabled(entity.location.world) || FactionsEntityListener.canDamage(player, entity, false)
    }
}
