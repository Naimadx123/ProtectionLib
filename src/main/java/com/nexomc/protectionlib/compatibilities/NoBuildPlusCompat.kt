package com.nexomc.protectionlib.compatibilities

import com.nexomc.protectionlib.ProtectionCompatibility
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import p1xel.nobuildplus.API.NBPAPI
import p1xel.nobuildplus.Flags
import p1xel.nobuildplus.NoBuildPlus

class NoBuildPlusCompat(mainPlugin: JavaPlugin, plugin: NoBuildPlus) : ProtectionCompatibility<NoBuildPlus>(mainPlugin, plugin) {

    override fun canBuild(player: Player, target: Location): Boolean {
        return !plugin.api.canExecute(target.world.name, Flags.build)
    }

    override fun canBreak(player: Player, target: Location): Boolean {
        return !plugin.api.canExecute(target.world.name, Flags.destroy)
    }

    override fun canInteract(player: Player, target: Location): Boolean {
        return !plugin.api.canExecute(target.world.name, Flags.use)
    }

    override fun canUse(player: Player, target: Location): Boolean {
        return !plugin.api.canExecute(target.world.name, Flags.use)
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        return !plugin.api.canExecute(entity.location.world.name, Flags.mob_damage)
    }
}
