fun main() {
    val world = HittableList()
    world.add(Sphere(Vec3(0, 0, -1), 0.5))
    world.add(Sphere(Vec3(0.0, -100.5, -1.0), 100.0))

    val cam = Camera()

    cam.aspectRatio = 16.0 / 9.0
    cam.imageWidth = 400
    cam.samplesPerPixel = 100

    cam.render(world)
}