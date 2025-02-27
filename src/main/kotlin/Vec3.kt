import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

class Vec3(var x: Double, var y: Double, var z: Double) {
    constructor() : this(0.0, 0.0, 0.0)
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
    fun nearZero(): Boolean {
        // Return true if the vector is close to zero in all dimensions
        val s = 1e-8
        return (abs(x) < s) && (abs(y) < s) && (abs(z) < s)
    }
    fun toColor() = Color(x, y, z)

    companion object {
        fun dot(u: Vec3, v: Vec3) = u.x*v.x + u.y*v.y + u.z*v.z
        fun cross(u: Vec3, v: Vec3) = Vec3(u.y * v.z - u.z * v.y,
                                                 u.z * v.x - u.x * v.z,
                                                 u.x * v.y - u.y * v.x)
        fun unitVector(v: Vec3) = v / v.length()
        fun randomInUnitDisk(): Vec3 {
            while(true) {
                val p = Vec3(randomDouble(-1.0, 1.0), randomDouble(-1.0, 1.0), 0.0)
                if(p.length() < 1)
                    return p
            }
        }
        fun random() = Vec3(Math.random(), Math.random(), Math.random())
        fun randomDouble(min: Double, max: Double) = min + (max - min) * Math.random()
        private fun randomInUnitSphere(): Vec3 {
            while(true) {
                val p = Vec3(randomDouble(-1.0, 1.0), randomDouble(-1.0, 1.0), randomDouble(-1.0, 1.0))
                if(p.lengthSquared() < 1)
                    return p
            }
        }
        fun randomUnitVector() = unitVector(randomInUnitSphere())
        fun reflect(v: Vec3, n: Vec3) = v - 2 * dot(v, n) * n
        fun refract(uv: Vec3, n: Vec3, etaiOverEtat: Double): Vec3 {
            val cosTheta = min(dot(-uv, n), 1.0)
            val rOutPerpendicular = etaiOverEtat * (uv + cosTheta * n)
            val rOutParallel = -sqrt(abs(1.0 - rOutPerpendicular.lengthSquared())) * n
            return rOutPerpendicular + rOutParallel
        }
        operator fun Color.times(c: Color) = (this.toVec3() * c.toVec3()).toColor()
        operator fun Double.times(v: Vec3) = v * this
        operator fun Double.times(c: Color) = (c.toVec3() * this).toColor()
    }
}