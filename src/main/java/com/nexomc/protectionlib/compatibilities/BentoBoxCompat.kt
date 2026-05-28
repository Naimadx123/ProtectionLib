package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import org.bukkit.Location
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import world.bentobox.bentobox.BentoBox
import world.bentobox.bentobox.api.flags.Flag
import world.bentobox.bentobox.api.user.User
import world.bentobox.bentobox.lists.Flags

class BentoBoxCompat(mainPlugin: JavaPlugin, plugin: BentoBox) : ProtectionCompatibility<BentoBox>(mainPlugin, plugin) {

    /**
     * @param player Player looking to place a block
     * @param target Place where the player seeks to place a block
     * @return true if he can put the block
     */
    override fun canBuild(player: Player, target: Location): Boolean {
        return canDo(player, target, Flags.PLACE_BLOCKS)
    }

    private fun canDo(player: Player, target: Location, flag: Flag): Boolean {
        return !plugin.iwm.inWorld(target) || plugin.islands.getIslandAt(target).map { it.isAllowed(User.getInstance(player), flag) }.orElse(!flag.isSetForWorld(target.world))
    }

    /**
     * @param player Player looking to break a block
     * @param target Place where the player seeks to break a block
     * @return true if he can break the block
     */
    override fun canBreak(player: Player, target: Location): Boolean {
        return canDo(player, target, Flags.BREAK_BLOCKS)
    }

    /**
     * @param player Player looking to interact with a block
     * @param target Place where the player seeks to interact with a block
     * @return true if he can interact with the block
     */
    override fun canInteract(player: Player, target: Location): Boolean {
        return !plugin.iwm.inWorld(target) || plugin.islands.locationIsOnIsland(player, target)
    }

    /**
     * @param player Player looking to use an item
     * @param target Place where the player seeks to use an item at a location
     * @return true if he can use the item at the location
     */
    override fun canUse(player: Player, target: Location): Boolean {
        return !plugin.iwm.inWorld(target) || plugin.islands.locationIsOnIsland(player, target)
    }

    /**
     * @param player Player looking to use an item
     * @param entity Entity that players trying to damage
     * @return true if he can damage the entity
     */
    override fun canDamage(player: Player, entity: Entity): Boolean {
        return when (entity) {
            is Animals -> canDo(player, entity.location, Flags.HURT_ANIMALS)
            is Monster -> canDo(player, entity.location, Flags.HURT_MONSTERS)
            is Villager -> canDo(player, entity.location, Flags.HURT_VILLAGERS)

            is Player -> {
                val iwm = plugin.iwm
                val world = entity.location.world
                when {
                    iwm.inWorld(world) -> canDo(player, entity.location, Flags.PVP_OVERWORLD)
                    iwm.isNether(world) -> canDo(player, entity.location, Flags.PVP_NETHER)
                    iwm.isEnd(world) -> canDo(player, entity.location, Flags.PVP_END)
                    else -> true
                }
            }

            else -> true
        }
    }
}
