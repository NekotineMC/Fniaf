package fr.nekotine.fniaf.animation.tree

import fr.nekotine.fniaf.animation.math.copy
import fr.nekotine.fniaf.animation.observer.Observable
import org.joml.Quaterniond
import org.joml.Vector3d

class KinematicJoint (
    var parent:KinematicJoint?,
    var children:ArrayList<KinematicJoint>,
    var effectors: ArrayList<EndEffector>,
    var name: String?,
    var localPosition: Vector3d,
    var localRotation: Quaterniond,
    var axis: Vector3d?,
    var clampedAngleDegrees: Pair<Double,Double>?
) : Observable()
{
    constructor(): this(null, ArrayList(), ArrayList(), "", Vector3d(), Quaterniond(), null,null)

    var globalPosition: Vector3d = localPosition.copy()
    var globalRotation: Quaterniond = localRotation.copy()

    //update world values starting with self
    fun updateWorldValues() {
        //update self
        if(parent == null) {
            globalRotation.set(localRotation)
        } else {
            globalPosition.set(parent!!.globalPosition.copy().add(localPosition.copy().rotate(parent!!.globalRotation)))
            globalRotation.set(parent!!.globalRotation.copy().mul(localRotation))
        }

        //update children
        this.children.forEach { child -> child.updateWorldValues() }

        //update end effectors
        this.effectors.forEach { effector ->
            effector.globalPosition.set(globalPosition.copy().add(effector.localPosition.copy().rotate(globalRotation)))
        }

        notifySubscribers()
        this.effectors.forEach { effector -> effector.notifySubscribers()}
    }
}