package fr.nekotine.fniaf.animation.tree

import fr.nekotine.fniaf.animation.math.copy
import org.joml.Quaterniond
import org.joml.Vector3d

class KinematicTreeBuilder (
    private val name: String,
    private val localPosition: Vector3d,
    private val localRotation: Quaterniond,
    private val axis: Vector3d?,
    private val clampedAngleDegrees: Pair<Double,Double>?
) {
    private val children: ArrayList<KinematicJoint> = ArrayList()
    private val effectors: ArrayList<EndEffector> = ArrayList()

    fun addNode(joint: KinematicJoint): KinematicTreeBuilder {
        children.add(joint)
        return this
    }

    fun addEffector(name: String, localPosition: Vector3d): KinematicTreeBuilder {
        effectors.add(EndEffector(null, name, localPosition))
        return this
    }

    /**
     * Add an end effector and stores the created end effector in the given parameter
     * @param endEffector The variable to be filled
     */
    fun addEffector(name:String, localPosition: Vector3d, endEffector: EndEffector): KinematicTreeBuilder {
        endEffector.name = name
        endEffector.localPosition = localPosition
        effectors.add(endEffector)
        return this
    }

    fun build(): KinematicJoint {
        val joint = KinematicJoint(null, children, effectors, name, localPosition, localRotation, axis, clampedAngleDegrees)
        children.forEach { children -> children.parent = joint }
        effectors.forEach { effector -> effector.parent = joint }
        joint.updateWorldValues()
        return joint
    }

    /**
     * Build the tree and stores the built root node in the given parameter
     * @param joint The variable to be filled
     * @return joint
     */
    fun build(joint:KinematicJoint): KinematicJoint {
        joint.parent = null
        joint.children = children
        joint.effectors = effectors
        joint.name = name
        joint.localPosition = localPosition
        joint.localRotation = localRotation
        joint.axis = axis
        joint.clampedAngleDegrees = clampedAngleDegrees
        joint.globalPosition = localPosition.copy()
        joint.globalRotation = localRotation.copy()
        children.forEach { children -> children.parent = joint }
        effectors.forEach { effector -> effector.parent = joint }
        joint.updateWorldValues()
        return joint
    }
}