package fr.nekotine.fniaf.animation.tree

import org.joml.AxisAngle4d
import org.joml.Math
import org.joml.Quaterniond
import org.joml.Vector3d

fun Quaterniond.toAxisAngle(): AxisAngle4d {
    val angle = 2.0 * Math.acos(this.w)
    val sinHalfAngle = Math.sqrt(1.0 - this.w * this.w)
    if (sinHalfAngle < 1e-6) {
        return AxisAngle4d(angle,1.0, 0.0, 0.0)
    }
    return AxisAngle4d(angle, this.x / sinHalfAngle, this.y / sinHalfAngle, this.z / sinHalfAngle)
}
fun Quaterniond.copy(): Quaterniond {
    return Quaterniond(this)
}
fun Vector3d.copy(): Vector3d {
    return Vector3d(this)
}