package fr.nekotine.fniaf.animation.math

import org.joml.*

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
fun Vector3d.toVector3f(): Vector3f {
    return Vector3f(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
}
fun Quaterniond.toQuaternionf(): Quaternionf {
    return Quaternionf(this)
}