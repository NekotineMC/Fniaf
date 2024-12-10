package fr.nekotine.fniaf.animation.humanoid

import fr.nekotine.fniaf.FnIAf
import fr.nekotine.fniaf.animation.math.toQuaternionf
import fr.nekotine.fniaf.animation.math.toVector3f
import fr.nekotine.fniaf.animation.observer.Observable
import fr.nekotine.fniaf.animation.observer.Observer
import fr.nekotine.fniaf.animation.tree.KinematicJoint
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

class JointRenderer (
    private val joint: KinematicJoint,
    private val texture: Material,
    private val scale: Double,
    private val world: World,
    private val interpolationDuration: Int,
) : Observer {
    private val jointDisplay: ItemDisplay =
        world.spawnEntity(Location(world, .0, .0, .0), EntityType.ITEM_DISPLAY) as ItemDisplay

    init {
        joint.subscribe(this)
        jointDisplay.interpolationDuration = interpolationDuration
        jointDisplay.interpolationDelay = 1
        jointDisplay.setItemStack(ItemStack(texture))
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
        jointDisplay.transformation = Transformation(
            joint.globalPosition.toVector3f(),
            joint.globalRotation.toQuaternionf(),
            Vector3f(scale.toFloat()),
            Quaternionf()
        )
    }

    fun show() {
        jointDisplay.isInvisible = false
    }

    fun hide() {
        jointDisplay.isInvisible = true
    }
}