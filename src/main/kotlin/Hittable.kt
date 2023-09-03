import Vec3.Companion.dot

class HitRecord {
    var p = Vec3()
    var normal = Vec3()
    var mat: Material = Lambertian(Color(1, 1, 1))
    var t = 0.0
    var frontFace: Boolean = false

    fun setFaceNormal(r: Ray, outwardNormal: Vec3) {
        frontFace = dot(r.direction, outwardNormal) < 0
        normal = if(frontFace) outwardNormal else -outwardNormal
    }
}

interface Hittable {
    fun hit(r: Ray, rayT: Interval, rec: HitRecord): Boolean
}