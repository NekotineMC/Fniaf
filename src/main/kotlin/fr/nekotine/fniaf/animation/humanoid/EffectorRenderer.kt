package fr.nekotine.fniaf.animation.humanoid

import fr.nekotine.fniaf.FnIAf
import fr.nekotine.fniaf.animation.math.toVector3f
import fr.nekotine.fniaf.animation.observer.Observable
import fr.nekotine.fniaf.animation.observer.Observer
import fr.nekotine.fniaf.animation.tree.EndEffector
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f

class EffectorRenderer (
    private val effector: EndEffector,
    private val texture: Material,
    private val scale: Double,
    private val world: World,
    private val interpolationDuration: Int,
) : Observer {
    private val effectorDisplay: ItemDisplay =
        world.spawnEntity(Location(world, .0, .0, .0), EntityType.ITEM_DISPLAY) as ItemDisplay

    init {
        effector.subscribe(this)
        effectorDisplay.interpolationDuration = interpolationDuration
        effectorDisplay.interpolationDelay = 1
        effectorDisplay.setItemStack(ItemStack(texture))
        Bukkit.getScheduler().runTaskLater(FnIAf.javaPlugin, Runnable {
            run {
                updateDisplay()
            }
        }, 20)
    }

    override fun update(observable: Observable) {
        updateDisplay()
    }

    private fun updateDisplay() {
        effectorDisplay.transformation = Transformation(
            effector.globalPosition.toVector3f(),
            Quaternionf(),
            Vector3f(scale.toFloat()),
            Quaternionf()
        )
    }

    fun show() {
        effectorDisplay.isInvisible = false
    }

    fun hide() {
        effectorDisplay.isInvisible = true
    }
}