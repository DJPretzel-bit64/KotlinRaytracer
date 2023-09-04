import Vec3.Companion.cross
import Vec3.Companion.randomInUnitDisk
import Vec3.Companion.times
import Vec3.Companion.unitVector
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.tan

class Camera {
    var aspectRatio = 1.0
    var imageWidth = 100
    var samplesPerPixel = 10
    var maxDepth = 10
    var vFov = 90.0
    var lookFrom = Vec3(0.0, 0.0, -1.0)
    var lookAt = Vec3(0.0, 0.0, 0.0)
    var vup = Vec3(0.0, 1.0, 0.0)
    var defocusAngle = 0.0
    var focusDist = 10.0
    private var imageHeight = 100
    private var center = Vec3()
    private var pixel00Loc = Vec3()
    private var pixelDeltaU = Vec3()
    private var pixelDeltaV = Vec3()
    private var u = Vec3()
    private var v = Vec3()
    private var w = Vec3()
    private var defocusDiskU = Vec3()
    private var defocusDiskV = Vec3()
    private var viewportHeight = 0.0
    private var viewportWidth = 0.0
    private var viewportU = Vec3()
    private var viewportV = Vec3()
    private var viewportUpperLeft = Vec3()
    private var render: BufferedImage? = null
    private var output: File? = null

    fun render(world: Hittable) {
        initialize()

//        print("P3\n$imageWidth $imageHeight\n255\n")

        for(j in 0..<imageHeight) {
            for(i in 0..<imageWidth) {
                var pixelColor = Color(0, 0, 0)
                for(sample in 0..<samplesPerPixel) {
                    val r = getRay(i, j)
                    pixelColor += rayColor(r, maxDepth, world)
                }
                Color.writeColor(pixelColor, samplesPerPixel, render, i, j)
            }
        }

        ImageIO.write(render, "png", output)
    }

    private fun initialize() {
        imageHeight = max((imageWidth / aspectRatio).toInt(), 1)

        center = lookFrom

        //Determine viewport dimensions.
        val theta = Math.toRadians(vFov)
        val h = tan(theta/2)
        viewportHeight = 2.0 * h * focusDist
        viewportWidth = viewportHeight * (imageWidth / imageHeight.toDouble())

        // Calculate the u, v, w unit basis vectors for the camera coordinate frame.
        w = unitVector(lookFrom - lookAt)
        u = unitVector(cross(vup, w))
        v = cross(w, u)

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        viewportU = viewportWidth * u
        viewportV = viewportHeight * -v

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        pixelDeltaU = viewportU / imageWidth.toDouble()
        pixelDeltaV = viewportV / imageHeight.toDouble()

        // Calculate the location of the upper left pixel.
        viewportUpperLeft = center - (focusDist * w) - viewportU / 2.0 - viewportV / 2.0
        pixel00Loc = viewportUpperLeft + 0.5 * (pixelDeltaU + pixelDeltaV)

        // Calculate the camera defocus disk basis vectors.
        val defocusRadius = focusDist * tan(Math.toRadians(defocusAngle / 2.0))
        defocusDiskU = u * defocusRadius
        defocusDiskV = v * defocusRadius

        render = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR)
        output = File("render.png")
    }

    private fun getRay(i: Int, j: Int): Ray {
        // Get a randomly sampled camera ray for the pixel at location i, j, originating from
        // the camera defocus disk.

        val pixelCenter = pixel00Loc + (i.toDouble() * pixelDeltaU) + (j.toDouble() * pixelDeltaV)
        val pixelSample = pixelCenter + pixelSampleSquare()

        val rayOrigin = if(defocusAngle <= 0) center else defocusDiskSample()
        val rayDirection = pixelSample - rayOrigin

        return Ray(rayOrigin, rayDirection)
    }

    private fun pixelSampleSquare(): Vec3 {
        // Returns a random point in the square surrounding a pixel at the origin.
        val px = -0.5 + Math.random()
        val py = -0.5 + Math.random()
        return (px * pixelDeltaU) + (py * pixelDeltaV)
    }

    private fun rayColor(r: Ray, depth: Int, world: Hittable): Color {
        val rec = HitRecord()

        // If we've exceeded the ray bounce limit, no more light is gathered.
        if(depth <= 0)
            return Color(0, 0, 0)

        if(world.hit(r, Interval(0.001, Double.POSITIVE_INFINITY), rec)) {
            val scattered = mutableListOf(Ray())
            val attenuation = mutableListOf(Color())
            if(rec.mat.scatter(r, rec, attenuation, scattered))
                return attenuation[0] * rayColor(scattered[0], depth-1, world)
            return Color()
        }

        val unitDirection = unitVector(r.direction)
        val a = 0.5 * (unitDirection.y + 1.0)
        return ((1.0 - a) * Color(1.0, 1.0, 1.0).toVec3() + a * Color(0.5, 0.7, 1.0).toVec3()).toColor()
    }
    private fun defocusDiskSample(): Vec3 {
        // Returns a random point in the camera defocus disk.
        val p = randomInUnitDisk()
        return center + (p.x * defocusDiskU) + (p.y * defocusDiskV)
    }
}