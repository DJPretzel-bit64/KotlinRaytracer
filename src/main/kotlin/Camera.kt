import Vec3.Companion.times
import Vec3.Companion.unitVector
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max

class Camera {
    var aspectRatio = 1.0
    var imageWidth = 100
    var samplesPerPixel = 10
    private var imageHeight = 100
    private var center = Vec3()
    private var pixel00Loc = Vec3()
    private var pixelDeltaU = Vec3()
    private var pixelDeltaV = Vec3()
    private var focalLength = 0.0
    private var viewportHeight = 0.0
    private var viewportWidth = 0.0
    private var viewportU = Vec3()
    private var viewportV = Vec3()
    private var viewportUpperLeft = Vec3()
    private var image: BufferedImage? = null
    private var output: File? = null


    fun render(world: Hittable) {
        initialize()

        print("P3\n$imageWidth $imageHeight\n255\n")

        for(j in 0..<imageHeight) {
            for(i in 0..<imageWidth) {
                var pixelColor = Color(0, 0, 0)
                for(sample in 0..<samplesPerPixel) {
                    val r = getRay(i, j)
                    pixelColor += rayColor(r, world)
                }
                Color.writeColor(pixelColor, samplesPerPixel)
                image?.setRGB(i, j, Color.intRGB((pixelColor.toVec3() / samplesPerPixel.toDouble()).toColor()))
            }
        }

        ImageIO.write(image, "PNG", output)
    }

    private fun initialize() {
        imageHeight = max((imageWidth / aspectRatio).toInt(), 1)

        center = Vec3(0.0, 0.0, 0.0)

        //Determine viewport dimensions.
        focalLength = 1.0
        viewportHeight = 2.0
        viewportWidth = viewportHeight * (imageWidth / imageHeight.toDouble())

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        viewportU = Vec3(viewportWidth, 0.0, 0.0)
        viewportV = Vec3(0.0, -viewportHeight, 0.0)

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        pixelDeltaU = viewportU / imageWidth.toDouble()
        pixelDeltaV = viewportV / imageHeight.toDouble()

        // Calculate the location of the upper left pixel.
        viewportUpperLeft = center - Vec3(0.0, 0.0, focalLength) - viewportU / 2.0 - viewportV / 2.0
        pixel00Loc = viewportUpperLeft + 0.5 * (pixelDeltaU + pixelDeltaV)

        image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR)
        output = File("render.png")
    }

    private fun getRay(i: Int, j: Int): Ray {
        // Get a randomly sampled camera ray for the pixel at location i, j

        val pixelCenter = pixel00Loc + (i.toDouble() * pixelDeltaU) + (j.toDouble() * pixelDeltaV)
        val pixelSample = pixelCenter + pixelSampleSquare()

        val rayOrigin = center
        val rayDirection = pixelSample - rayOrigin

        return Ray(rayOrigin, rayDirection)
    }

    private fun pixelSampleSquare(): Vec3 {
        // Returns a random point in the square surrounding a pixel at the origin.
        val px = -0.5 + Math.random()
        val py = -0.5 + Math.random()
        return (px * pixelDeltaU) + (py * pixelDeltaV)
    }

    private fun rayColor(r: Ray, world: Hittable): Color {
        val rec = HitRecord()

        if(world.hit(r, Interval(0.0, Double.POSITIVE_INFINITY), rec))
            return (0.5 * (rec.normal + Color(1, 1, 1).toVec3())).toColor()

        val unitDirection = unitVector(r.direction)
        val a = 0.5 * (unitDirection.y + 1.0)
        return ((1.0 - a) * Color(1.0, 1.0, 1.0).toVec3() + a * Color(0.5, 0.7, 1.0).toVec3()).toColor()
    }
}