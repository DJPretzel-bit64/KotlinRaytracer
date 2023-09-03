class HittableList : Hittable {
    val objects = mutableListOf<Hittable>()

    fun clear() {
        objects.clear()
    }

    fun add(thing: Hittable) {
        objects.add(thing)
    }

    override fun hit(r: Ray, rayT: Interval, rec: HitRecord): Boolean {
        val tempRec = HitRecord()
        var hitAnything = false
        var closestSoFar = rayT.max

        for(thing: Hittable in objects) {
            if(thing.hit(r, Interval(rayT.min, closestSoFar), tempRec)) {
                hitAnything = true
                closestSoFar = tempRec.t
                rec.normal = tempRec.normal
            }
        }

        return hitAnything
    }
}