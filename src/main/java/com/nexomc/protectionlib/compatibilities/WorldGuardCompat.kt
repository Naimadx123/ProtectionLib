package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.LocalPlayer
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.Flags
import org.bukkit.Location
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class WorldGuardCompat(mainPlugin: JavaPlugin, plugin: WorldGuardPlugin) : ProtectionCompatibility<WorldGuardPlugin>(mainPlugin, plugin) {
    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        val localPlayer = plugin.wrapPlayer(player)
        return WorldGuard.getInstance().platform.regionContainer.createQuery().testBuild(BukkitAdapter.adapt(target), localPlayer, Flags.BLOCK_PLACE) || hasBypass(player, localPlayer)
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        val localPlayer = plugin.wrapPlayer(player)
        return WorldGuard.getInstance().platform.regionContainer.createQuery().testBuild(BukkitAdapter.adapt(target), localPlayer, Flags.BLOCK_BREAK) || hasBypass(player, localPlayer)
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        val localPlayer = plugin.wrapPlayer(player)
        return WorldGuard.getInstance().platform.regionContainer.createQuery().testBuild(BukkitAdapter.adapt(target), localPlayer, Flags.INTERACT) || hasBypass(player, localPlayer)
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        val localPlayer = plugin.wrapPlayer(player)
        return WorldGuard.getInstance().platform.regionContainer.createQuery().testBuild(BukkitAdapter.adapt(target), localPlayer, Flags.USE) || hasBypass(player, localPlayer)
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        val localPlayer = plugin.wrapPlayer(player)
        val query = WorldGuard.getInstance().platform.regionContainer.createQuery()
        val location = BukkitAdapter.adapt(entity.location)

        val flag = when (entity) {
            is Player -> Flags.PVP
            is Animals -> Flags.DAMAGE_ANIMALS
            is Monster -> Flags.DAMAGE_ANIMALS
            else -> Flags.DAMAGE_ANIMALS
        }

        return query.testState(location, localPlayer, flag) || hasBypass(player, localPlayer)
    }

    private fun hasBypass(player: Player, localPlayer: LocalPlayer): Boolean {
        return WorldGuard.getInstance().platform.sessionManager.hasBypass(localPlayer, BukkitAdapter.adapt(player.world))
    }
}
