import Vec3.Companion.dot
import kotlin.math.sqrt

class Sphere(private val center: Vec3, private val radius: Double) : Hittable {
    override fun hit(r: Ray, rayT: Interval, rec: HitRecord): Boolean {
        val oc = r.origin - center
        val a = r.direction.lengthSquared()
        val halfB = dot(oc, r.direction)
        val c = oc.lengthSquared() - radius * radius

        val discriminant = halfB*halfB - a*c
        if(discriminant < 0) return false
        val sqrtD = sqrt(discriminant)

        // Find the nearest root that lies in the acceptable range.
        var root = (-halfB - sqrtD) / a
        if(!rayT.surrounds(root)) {
            root = (-halfB + sqrtD) / a
            if(!rayT.surrounds(root))
                return false
        }

        rec.t = root
        rec.p = r.at(rec.t)
        val outwardNormal = (rec.p - center) / radius
        rec.setFaceNormal(r, outwardNormal)

        return true
    }
}