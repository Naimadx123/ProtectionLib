package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import com.plotsquared.core.plot.Plot
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class PlotSquaredCompat(mainPlugin: JavaPlugin, plugin: Plugin) : ProtectionCompatibility<Plugin>(mainPlugin, plugin) {
    override fun canBuild(player: Player, target: Location): Boolean {
        return target.plotFromLocation()?.isDenied(player.uniqueId) != true
    }

    override fun canBreak(player: Player, target: Location): Boolean {
        return target.plotFromLocation()?.isDenied(player.uniqueId) != true
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        return target.plotFromLocation()?.isDenied(player.uniqueId) != true
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        return target.plotFromLocation()?.isDenied(player.uniqueId) != true
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        return entity.location.plotFromLocation()?.isDenied(player.uniqueId) != true
    }

    private fun Location.plotFromLocation(): Plot? {
        return Plot.getPlot(adaptBukkitLocation(this) ?: return null)
    }

    private fun adaptBukkitLocation(location: Location): com.plotsquared.core.location.Location? {
        if (!location.isWorldLoaded) return null
        return com.plotsquared.core.location.Location.at(location.world.name, location.blockX, location.blockY, location.blockZ)
    }
}
