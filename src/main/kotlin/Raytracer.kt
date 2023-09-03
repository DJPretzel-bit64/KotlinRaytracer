fun main() {
    val world = HittableList()

    val materialGround = Lambertian(Color(0.8, 0.8, 0.0))
    val materialCenter = Lambertian(Color(0.7, 0.3, 0.3))
    val materialLeft   = Metal(Color(0.8, 0.8, 0.8), 0.3)
    val materialRight  = Metal(Color(0.8, 0.6, 0.2), 1.0)

    world.add(Sphere(Vec3( 0.0, -100.5, -1.0), 100.0, materialGround))
    world.add(Sphere(Vec3( 0.0,    0.0, -1.0),   0.5, materialCenter))
    world.add(Sphere(Vec3(-1.0,    0.0, -1.0),   0.5, materialLeft))
    world.add(Sphere(Vec3( 1.0,    0.0, -1.0),   0.5, materialRight))

    val cam = Camera()

    cam.aspectRatio = 16.0 / 9.0
    cam.imageWidth = 400
    cam.samplesPerPixel = 100
    cam.maxDepth = 50

    cam.render(world)
}