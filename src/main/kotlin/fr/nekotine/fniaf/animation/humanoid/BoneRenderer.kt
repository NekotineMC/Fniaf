package fr.nekotine.fniaf.animation.humanoid

import fr.nekotine.core.NekotinePlugin
import fr.nekotine.fniaf.FnIAf
import fr.nekotine.fniaf.animation.math.copy
import fr.nekotine.fniaf.animation.math.toQuaternionf
import fr.nekotine.fniaf.animation.math.toVector3f
import fr.nekotine.fniaf.animation.observer.Observable
import fr.nekotine.fniaf.animation.observer.Observer
import fr.nekotine.fniaf.animation.tree.EndEffector
import fr.nekotine.fniaf.animation.tree.KinematicJoint
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3d

class BoneRenderer (
    private val joint: KinematicJoint?,
    private val effector: EndEffector?,
    private val texture: Material,
    private val sideLength: Double,
    private val world: World,
    private val interpolationDuration: Int,
) : Observer {
    constructor(joint: KinematicJoint, texture: Material, sideLength: Double, world: World, interpolationDuration: Int): this(
        joint, null, texture, sideLength, world, interpolationDuration
    )
    constructor(effector: EndEffector, texture: Material, sideLength: Double, world: World, interpolationDuration: Int): this(
        null, effector, texture, sideLength, world, interpolationDuration
    )

    private val boneDisplay: ItemDisplay = world.spawnEntity(Location(world,.0, .0 , .0 ), EntityType.ITEM_DISPLAY) as ItemDisplay

    init {
        joint?.subscribe(this)
        effector?.subscribe(this)
        boneDisplay.interpolationDuration = interpolationDuration
        boneDisplay.interpolationDelay = 1
        boneDisplay.setItemStack(ItemStack(texture))
        Bukkit.getScheduler().runTaskLater(FnIAf.javaPlugin, Runnable {
            run {
                updateBone()
            }
        }, 20)
    }

    override fun update(observable: Observable) {
        updateBone()
    }

    private fun updateBone() {
        if(joint?.parent != null) {
            updateBone(joint.localPosition, joint.globalPosition, joint.parent!!.globalPosition)
        }
        else if(effector?.parent != null) {
            updateBone(effector.localPosition, effector.globalPosition, effector.parent!!.globalPosition)
        }
    }

    private fun updateBone(localPosition: Vector3d, globalPosition: Vector3d, parentGlobalPosition: Vector3d) {
        val rotation = Quaterniond()
        val length = localPosition.length()
        val scale = Vector3d(length, sideLength, sideLength)
        val direction = parentGlobalPosition.copy().sub(globalPosition)
        val translation = parentGlobalPosition.copy().sub(direction.copy().mul(.5))
        Vector3d(1.0,.0,.0).rotationTo(direction, rotation)

        boneDisplay.transformation = Transformation(translation.toVector3f(), rotation.toQuaternionf(), scale.toVector3f(), Quaternionf())
    }
    fun show(){
        boneDisplay.isInvisible = false
    }
    fun hide(){
        boneDisplay.isInvisible = true
    }
}