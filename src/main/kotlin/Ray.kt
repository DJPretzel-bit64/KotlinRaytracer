import Vec3.Companion.times

class Ray(val origin: Vec3, val direction: Vec3) {
    constructor() : this(Vec3(), Vec3())

    fun at(t: Double) = origin + t * direction
}

