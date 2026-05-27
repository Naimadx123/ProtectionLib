package com.nexomc.protectionlib

import com.nexomc.protectionlib.compatibilities.BentoBoxCompat
import com.nexomc.protectionlib.compatibilities.CrashClaimCompat
import com.nexomc.protectionlib.compatibilities.FactionsUuidCompat
import com.nexomc.protectionlib.compatibilities.GriefPreventionCompat
import com.nexomc.protectionlib.compatibilities.HuskClaimCompat
import com.nexomc.protectionlib.compatibilities.HuskTownsCompat
import com.nexomc.protectionlib.compatibilities.LandsCompat
import com.nexomc.protectionlib.compatibilities.NoBuildPlusCompat
import com.nexomc.protectionlib.compatibilities.PlotSquaredCompat
import com.nexomc.protectionlib.compatibilities.ResidenceCompat
import com.nexomc.protectionlib.compatibilities.TownyCompat
import com.nexomc.protectionlib.compatibilities.WorldGuardCompat
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

object ProtectionLib {
    private val compatibilities: MutableSet<ProtectionCompatibility<out Plugin>> = ObjectOpenHashSet()
    @JvmField
    var debug: Boolean = false

    @JvmStatic
    fun init(plugin: JavaPlugin) {
        handleCompatibility("WorldGuard", plugin, ::WorldGuardCompat)
        handleCompatibility("Towny", plugin, ::TownyCompat)
        handleCompatibility("Factions", plugin, ::FactionsUuidCompat)
        handleCompatibility("Lands", plugin, ::LandsCompat)
        handleCompatibility("PlotSquared", plugin, ::PlotSquaredCompat)
        handleCompatibility("CrashClaim", plugin, ::CrashClaimCompat)
        handleCompatibility("GriefPrevention", plugin, ::GriefPreventionCompat)
        handleCompatibility("HuskClaims", plugin, ::HuskClaimCompat)
        handleCompatibility("BentoBox", plugin, ::BentoBoxCompat)
        handleCompatibility("HuskTowns", plugin, ::HuskTownsCompat)
        handleCompatibility("Residence", plugin, ::ResidenceCompat)
        handleCompatibility("NoBuildPlus", plugin, ::NoBuildPlusCompat)
    }

    @JvmStatic
    fun canBuild(player: Player, target: Location): Boolean {
        return runCatching {
            compatibilities.all { it.canBuild(player, target) }
        }.onFailure { if (debug) it.printStackTrace() }.getOrDefault(true)
    }

    @JvmStatic
    fun canBreak(player: Player, target: Location): Boolean {
        return runCatching {
            compatibilities.all { it.canBreak(player, target) }
        }.onFailure { if (debug) it.printStackTrace() }.getOrDefault(true)
    }

    @JvmStatic
    fun canInteract(player: Player, target: Location): Boolean {
        return runCatching {
            compatibilities.all { it.canInteract(player, target) }
        }.onFailure { if (debug) it.printStackTrace() }.getOrDefault(true)
    }

    @JvmStatic
    fun canUse(player: Player, target: Location): Boolean {
        return runCatching {
            compatibilities.all { it.canUse(player, target) }
        }.onFailure { if (debug) it.printStackTrace() }.getOrDefault(true)
    }

    @JvmStatic
    fun canDamage(player: Player, entity: Entity): Boolean {
        return runCatching {
            compatibilities.all { it.canDamage(player, entity) }
        }.onFailure { if (debug) it.printStackTrace() }.getOrDefault(true)
    }

    private inline fun <reified T : Plugin> handleCompatibility(pluginName: String, mainPlugin: JavaPlugin, constructor: CompatibilityConstructor<T>) {
        runCatching {
            val plugin = Bukkit.getPluginManager().getPlugin(pluginName) as? T ?: return
            if (pluginName == "Factions" && !checkFactionsCompat()) return
            compatibilities.add(constructor.create(mainPlugin, plugin) ?: return@runCatching)
        }.onFailure {
            if (debug) it.printStackTrace()
        }
    }

    private fun checkFactionsCompat(): Boolean {
        return runCatching {
            Class.forName("com.massivecraft.factions.perms.PermissibleActions")
        }.onFailure {
            Bukkit.getLogger().warning("It seems a Factions plugin is installed, but it is not FactionsUUID.")
            Bukkit.getLogger().warning("ProtectionLib will not be able to handle Factions protection.")
        }.isSuccess
    }

    private fun interface CompatibilityConstructor<T : Plugin> {
        fun create(mainPlugin: JavaPlugin, plugin: T): ProtectionCompatibility<T>?
    }
}
