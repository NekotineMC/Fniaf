package fr.xxgoldenbluexx.pvpKotlin.building

import fr.xxgoldenbluexx.pvpKotlin.extension.getAllGoals
import fr.xxgoldenbluexx.pvpKotlin.extension.removeGoal
import fr.xxgoldenbluexx.pvpKotlin.extension.spawnEntity
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.entity.Ghast
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class Turet(override val di: DI, val world: World) : Listener, AutoCloseable, DIAware {

    private val ghast = Location(world, 164.0, 60.0, 180.0).spawnEntity(EntityType.GHAST) as Ghast

    private val plugin: JavaPlugin by instance()

    fun enable() {
        plugin.server.also {
            it.pluginManager.registerEvents(this, plugin)
        }
        for (goal in ghast.getAllGoals()) {
            ghast.removeGoal(goal)
        }
        val kbres = ghast.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
        kbres?.modifiers?.add(AttributeModifier(NamespacedKey(plugin,"turetnokb"), 1.0,AttributeModifier.Operation.ADD_NUMBER))
    }

    override fun close() {
        HandlerList.unregisterAll(this)
        ghast.remove()
    }
}