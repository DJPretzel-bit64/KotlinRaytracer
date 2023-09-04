fun main() {
    val world = HittableList()
    val groundMaterial = Lambertian(Color(0.5, 0.5, 0.5))
    world.add(Sphere(Vec3(0.0, -1000.0, 0.0), 1000.0, groundMaterial))

    for(a in -11..<11) {
        for(b in -11..<11) {
            val chooseMat = Math.random()
            val center = Vec3(a + 0.9 * Math.random(), 0.2, b + 0.9 * Math.random())

            if((center - Vec3(4.0, 0.2, 0.0)).length() > 0.9) {
                val sphereMaterial = mutableListOf<Material>(Lambertian(Color()))

                if(chooseMat < 0.8) {
                    // diffuse
                    val albedo = (Vec3.random() * Vec3.random()).toColor()
                    sphereMaterial[0] = Lambertian(albedo)
                    world.add(Sphere(center, 0.2, sphereMaterial[0]))
                } else if(chooseMat < 0.95) {
                    // metal
                    val albedo = (Vec3.random() * Vec3.random()).toColor()
                    val fuzz = Vec3.randomDouble(0.0, 0.5)
                    sphereMaterial[0] = Metal(albedo, fuzz)
                    world.add(Sphere(center, 0.2, sphereMaterial[0]))
                } else {
                    // glass
                    sphereMaterial[0] = Dielectric(1.5)
                    world.add(Sphere(center, 0.2, sphereMaterial[0]))
                }
            }
        }
    }

    val material1 = Dielectric(1.5)
    world.add(Sphere(Vec3(0.0, 1.0, 0.0), 1.0, material1))

    val material2 = Lambertian(Color(0.4, 0.2, 0.1))
    world.add(Sphere(Vec3(-4.0, 1.0, 0.0), 1.0, material2))

    val material3 = Metal(Color(0.7, 0.6, 0.5), 0.0)
    world.add(Sphere(Vec3(4.0, 1.0, 0.0), 1.0, material3))

    val cam = Camera()

    cam.aspectRatio     = 16.0 / 9.0
    cam.imageWidth      = 1920
    cam.samplesPerPixel = 500
    cam.maxDepth        = 50

    cam.vFov            = 20.0
    cam.lookFrom        = Vec3(13.0, 2.0, 3.0)
    cam.lookAt          = Vec3(0.0, 0.0, 0.0)
    cam.vup             = Vec3(0.0, 1.0, 0.0)

    cam.defocusAngle    = 0.6
    cam.focusDist       = 10.0

    cam.render(world)
}