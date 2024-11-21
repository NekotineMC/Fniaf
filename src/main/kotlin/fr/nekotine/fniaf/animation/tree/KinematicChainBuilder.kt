package fr.nekotine.fniaf.animation.tree

import org.joml.Quaterniond
import org.joml.Vector3d

class KinematicChainBuilder {
    private val joints = ArrayList<KinematicChain.Joint>()

    fun addLocalJoint(localPosition:Vector3d, axis:Vector3d?, clampedAngleDegrees:Pair<Double,Double>?): KinematicChainBuilder {
        val globalPosition = localPosition.copy()
        if(joints.size != 0) {
            joints.last().globalPosition.copy().add(globalPosition)
        }
        joints.add(KinematicChain.Joint(localPosition, Quaterniond(), axis, clampedAngleDegrees))
        return this
    }

    fun addLocalEndEffector(localPosition: Vector3d): KinematicChain? {
        if(joints.size == 0) {
            return null
        }
        return KinematicChain(joints, localPosition)
    }

    private fun Vector3d.copy():Vector3d {
        return Vector3d(this)
    }
}