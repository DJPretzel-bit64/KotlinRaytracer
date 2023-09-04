import Vec3.Companion.dot
import Vec3.Companion.randomUnitVector
import Vec3.Companion.reflect
import Vec3.Companion.refract
import Vec3.Companion.unitVector
import Vec3.Companion.times
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

interface Material {
    fun scatter(rIn: Ray, rec: HitRecord, attenuation: MutableList<Color>, scattered: MutableList<Ray>): Boolean
}

class Lambertian(private val albedo: Color) : Material {
    override fun scatter(rIn: Ray, rec: HitRecord, attenuation: MutableList<Color>, scattered: MutableList<Ray>): Boolean {
        var scatterDirection = rec.normal + randomUnitVector()

        //Catch degenerate scatter directions
        if(scatterDirection.nearZero()) {
            scatterDirection = rec.normal
        }

        scattered[0] = Ray(rec.p, scatterDirection)
        attenuation[0] = albedo
        return true
    }
}

class Metal(private val albedo: Color, f: Double) : Material {
    private val fuzz = if(f < 1) f else 1.0
    override fun scatter(rIn: Ray, rec: HitRecord, attenuation: MutableList<Color>, scattered: MutableList<Ray>): Boolean {
        val reflected = reflect(unitVector(rIn.direction), rec.normal)
        scattered[0] = Ray(rec.p, reflected + fuzz * randomUnitVector())
        attenuation[0] = albedo
        return (dot(scattered[0].direction, rec.normal) > 0)
    }
}

class Dielectric(private val ir: Double): Material {
    override fun scatter(rIn: Ray, rec: HitRecord, attenuation: MutableList<Color>, scattered: MutableList<Ray>): Boolean {
        attenuation[0] = Color(1.0, 1.0, 1.0)
        val refractionRatio = if(rec.frontFace) (1.0 / ir) else ir

        val unitDirection = unitVector(rIn.direction)
        val cosTheta = min(dot(-unitDirection, rec.normal), 1.0)
        val sinTheta = sqrt(1.0 - cosTheta * cosTheta)

        val cannotRefract = refractionRatio * sinTheta > 1.0

        val direction = if(cannotRefract || reflectance(cosTheta, refractionRatio) > Math.random()) reflect(unitDirection, rec.normal) else refract(unitDirection, rec.normal, refractionRatio)

        scattered[0] = Ray(rec.p, direction)
        return true
    }

    private fun reflectance(cosine: Double, refIdx: Double): Double {
        // Use Schlick's approximation for reflectance.
        var r0 = (1-refIdx) / (1+refIdx)
        r0 *= r0
        return r0 + (1-r0) * (1 - cosine).pow(5.0)
    }
}