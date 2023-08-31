import kotlin.math.pow
import kotlin.math.sqrt

open class Vec3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
    operator fun plus(v: Vec3) = Vec3(this.x + v.x, this.y + v.y, this.z + v.z)
    operator fun minus(v: Vec3) = Vec3(this.x - v.x, this.y - v.y, this.z - v.z)
    operator fun times(a: Double) = Vec3(this.x * a, this.y * a, this.z * a)
    operator fun times(v: Vec3) = Vec3(this.x * v.x, this.y * v.y, this.z * v.z)
    operator fun div(a: Double) = Vec3(this.x / a, this.y / a, this.z / a)
    operator fun unaryMinus() = Vec3(-this.x, -this.y, -this.z)
    operator fun get(i: Int) = when (i) {
        0 -> x
        1 -> y
        2 -> z
        else -> -1.0
    }
    operator fun plusAssign(v: Vec3) {
        this.x += v.x
        this.y += v.y
        this.z += v.z
    }
    operator fun timesAssign(t: Double) {
        this.x *= t
        this.y *= t
        this.z *= t
    }
    operator fun divAssign(t: Double) {
        this.timesAssign(1 / t)
    }
    override fun toString(): String {
        return "$x $y $z"
    }
    fun length(): Double {
        return sqrt(lengthSquared())
    }
    fun lengthSquared(): Double {
        return this.x.pow(2.0) + this.y.pow(2.0) + this.z.pow(2.0)
    }
    companion object {
        fun dot(u: Vec3, v: Vec3): Double = u.x * v.x + u.y * v.y + u.z + v.z
        fun cross(u: Vec3, v: Vec3): Vec3 = Vec3(
            u.y * v.z - u.z * v.y,
            u.z * v.x - u.x * v.z,
            u.x * v.y - u.y * v.x
        )

        fun unitVector(v: Vec3): Vec3 = v / v.length()
    }
}