import java.awt.image.BufferedImage
import kotlin.math.sqrt

class Color(var r: Double, var g: Double, var b: Double) {
    constructor(r: Int, g: Int, b: Int) : this(r.toDouble(), g.toDouble(), b.toDouble())
    constructor() : this(0.0, 0.0, 0.0)
    operator fun unaryMinus() = Color(-r, -g, -b)
    operator fun plus(c: Color) = Color(r + c.r, g + c.g, b + c.b)
    operator fun minus(c: Color) = this + -c
    fun toVec3() = Vec3(r, g, b)
    override fun toString(): String {
        return "$r, $g, $b"
    }
    companion object {
        private fun linearToGamma(linearComponent: Double) = sqrt(linearComponent)

        fun writeColor(pixelColor: Color, samplesPerPixel: Int, render: BufferedImage?, i: Int, j: Int) {
            var r = pixelColor.r
            var g = pixelColor.g
            var b = pixelColor.b

            // Divide the color by the number of samples
            val scale = 1.0 / samplesPerPixel
            r *= scale
            g *= scale
            b *= scale

            // Apply the linear to gamma transform
            r = linearToGamma(r)
            g = linearToGamma(g)
            b = linearToGamma(b)

            //Write the translated [0,255] value of each color component.
            val intensity = Interval(0.000, 0.999)
            val ri = (256 * intensity.clamp(r)).toInt()
            val gi = (256 * intensity.clamp(g)).toInt()
            val bi = (256 * intensity.clamp(b)).toInt()

            /*println(
                "$ri " +
                "$gi " +
                "$bi"
            )*/

            val rgb = ri.shl(16) + gi.shl(8) + bi

            render?.setRGB(i, j, rgb)
        }
    }
}