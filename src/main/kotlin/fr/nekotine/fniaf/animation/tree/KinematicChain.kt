package fr.nekotine.fniaf.animation.tree

import org.joml.*

class KinematicChain (
    val joints: ArrayList<Joint> = ArrayList<Joint>(),
    private val endEffectorLocalPosition: Vector3d,
){
    val endEffectorGlobalPosition = Vector3d()
    init {
        updateWorldPositions()
    }

    private fun updateWorldPositions() {
        val globalRotation = joints.first().rotation.copy()
        for(i in 1 ..< joints.size) {
            joints[i].globalPosition = joints[i - 1].globalPosition.copy().add(
                joints[i].localPosition.copy().rotate(globalRotation)
            )
            globalRotation.mul(joints[i].rotation, globalRotation).normalize()
        }
        this.endEffectorGlobalPosition.set(
            joints.last().globalPosition.copy().add(endEffectorLocalPosition.copy().rotate(globalRotation))
        )
    }

    fun ccdik(goal:Vector3d, maxIterationAttempts:Int = 20, solveSquaredDistanceThreshold:Double = .01):Pair<Boolean,Double> {
        val rotationTo = Quaterniond()
        for (r in 1..maxIterationAttempts) {
            for (i in joints.size - 1 downTo 0) {

                val distanceToGoal = endEffectorGlobalPosition.distanceSquared(goal)
                if(distanceToGoal <= solveSquaredDistanceThreshold) {
                    return Pair(true, distanceToGoal)
                }

                val joint = joints[i]

                val directionToEffector = endEffectorGlobalPosition.copy().sub(joint.globalPosition)
                val directionToGoal = goal.copy().sub(joint.globalPosition)
                directionToEffector.rotationTo(directionToGoal, rotationTo)
                rotationTo.mul(joint.rotation, joint.rotation).normalize()


                if(joint.axis != null) {
                    val currentAxis = joint.axis.copy().rotate(joint.rotation)
                    currentAxis.rotationTo(joint.axis, rotationTo)
                    rotationTo.mul(joint.rotation, joint.rotation).normalize()
                }

                if(joint.clampedAngleDegrees != null) {
                    val axisAngle = joint.rotation.toAxisAngle()
                    axisAngle.angle = Math.toRadians(Math.clamp(joint.clampedAngleDegrees.first, joint.clampedAngleDegrees.second, Math.toDegrees(axisAngle.angle)))
                    joint.rotation.rotationAxis(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z).normalize()
                }
                updateWorldPositions()
            }
        }
        return Pair(true, endEffectorGlobalPosition.distanceSquared(goal))
    }

    class Joint (
        val localPosition: Vector3d,
        val rotation: Quaterniond,
        val axis:Vector3d?,
        val clampedAngleDegrees: Pair<Double,Double>?,
    ) {
        var globalPosition = localPosition.copy()
    }
}