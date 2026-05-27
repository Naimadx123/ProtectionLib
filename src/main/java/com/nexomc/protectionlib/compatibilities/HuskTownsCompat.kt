package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import net.william278.husktowns.BukkitHuskTowns
import net.william278.husktowns.api.BukkitHuskTownsAPI
import net.william278.husktowns.api.HuskTownsAPI
import net.william278.husktowns.libraries.cloplib.operation.OperationType
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class HuskTownsCompat(mainPlugin: JavaPlugin, plugin: BukkitHuskTowns) : ProtectionCompatibility<BukkitHuskTowns>(mainPlugin, plugin) {
    private val commonAPI = HuskTownsAPI.getInstance()
    private val bukkitAPI = BukkitHuskTownsAPI.getInstance()

    override fun canBuild(player: Player, target: Location): Boolean {
        return isOperationAllowed(player, target, OperationType.BLOCK_PLACE)
    }

    override fun canBreak(player: Player, target: Location): Boolean {
        return isOperationAllowed(player, target, OperationType.BLOCK_BREAK)
    }

    override fun canInteract(player: Player, target: Location): Boolean {
        val type = if (target.block.type.isBlock) OperationType.BLOCK_INTERACT else OperationType.ENTITY_INTERACT
        return isOperationAllowed(player, target, type)
    }

    override fun canUse(player: Player, target: Location): Boolean {
        return isOperationAllowed(player, target, OperationType.BLOCK_INTERACT)
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        val type = if (entity is Player) {
            OperationType.PLAYER_DAMAGE_PLAYER
        } else {
            OperationType.PLAYER_DAMAGE_ENTITY
        }

        return isOperationAllowed(player, entity.location, type)
    }

    private fun isOperationAllowed(player: Player, location: Location, type: OperationType): Boolean {
        val user = bukkitAPI.getOnlineUser(player)
        val position = bukkitAPI.getPosition(location)
        return commonAPI.isOperationAllowed(user, type, position)
    }
}
