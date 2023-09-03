import kotlin.math.sqrt

class Vec3(var x: Double, var y: Double, var z: Double) {
    constructor() : this(0.0, 0.0, 0.0)
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    operator fun unaryMinus() = Vec3(-x, -y, -z)
    operator fun get(i: Int) = when(i) {
        1 -> x
        2 -> y
        3 -> z
        else -> -1.0
    }
    operator fun plusAssign(v: Vec3) {
        x += v.x
        y += v.y
        z += v.z
    }
    operator fun timesAssign(t: Double) {
        x *= t
        y *= t
        z *= t
    }
    operator fun divAssign(t: Double) {
        x /= t
        y /= t
        z /= t
    }
    operator fun plus(v: Vec3) = Vec3(x + v.x, y + v.y, z + v.z)
    operator fun minus(v: Vec3) = this + -v
    operator fun times(v: Vec3) = Vec3(x*v.x, y*v.y, z*v.y)
    operator fun times(t: Double) = Vec3(x*t, y*t, z*t)
    operator fun div(t: Double) = this * (1/t)
    fun length() = sqrt(lengthSquared())
    fun lengthSquared() = x*x + y*y + z*z
    fun toColor() = Color(x, y, z)

    companion object {
        fun print(v: Vec3) {
            print("${v.x} ${v.y} ${v.z}")
        }
        fun dot(u: Vec3, v: Vec3) = u.x*v.x + u.y*v.y + u.z*v.z
        fun cross(u: Vec3, v: Vec3) = Vec3(u.y * v.z - u.z * v.y,
                                                 u.z * v.x - u.x * v.z,
                                                 u.x * v.y - u.y * v.x)
        fun unitVector(v: Vec3) = v / v.length()
        operator fun Double.times(v: Vec3) = v * this
        operator fun Double.times(c: Color) = (c.toVec3() * this).toColor()
    }
}