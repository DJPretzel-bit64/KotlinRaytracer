import Vec3.Companion.dot
import Vec3.Companion.randomUnitVector
import Vec3.Companion.reflect
import Vec3.Companion.unitVector
import Vec3.Companion.times

interface Material {
    fun scatter(rIn: Ray, rec: HitRecord, attenuation: MutableList<Color>, scattered: MutableList<Ray>): Boolean
}

class Lambertian(val albedo: Color) : Material {
    override fun scatter(rIn: Ray, rec: HitRecord, attenuation: MutableList<Color>, scattered: MutableList<Ray>): Boolean {
        var scatterDirection = rec.normal + Vec3.randomUnitVector()

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