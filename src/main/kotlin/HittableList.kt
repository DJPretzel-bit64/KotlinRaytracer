class HittableList : Hittable {
    private val objects = mutableListOf<Hittable>()

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
                rec.frontFace = tempRec.frontFace
                rec.t = tempRec.t
                rec.p = tempRec.p
                rec.mat = tempRec.mat
            }
        }

        return hitAnything
    }
}