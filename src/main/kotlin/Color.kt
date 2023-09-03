class Color(var r: Double, var g: Double, var b: Double) {
    constructor(r: Int, g: Int, b: Int) : this(r.toDouble(), g.toDouble(), b.toDouble())
    operator fun unaryMinus() = Color(-r, -g, -b)
    operator fun plus(c: Color) = Color(r + c.r, g + c.g, b + c.b)
    operator fun minus(c: Color) = this + -c
    fun toVec3() = Vec3(r, g, b)
    companion object {
        fun writeColor(pixelColor: Color, samplesPerPixel: Int) {
            var r = pixelColor.r
            var g = pixelColor.g
            var b = pixelColor.b

            // Divide the color by the number of samples
            val scale = 1.0 / samplesPerPixel
            r *= scale
            g *= scale
            b *= scale

            //Write the translated [0,255] value of each color component.
            val intensity = Interval(0.000, 0.999)
            println(
                "${(256 * intensity.clamp(r)).toInt()} " +
                "${(256 * intensity.clamp(g)).toInt()} " +
                "${(256 * intensity.clamp(b)).toInt()}"
            )
        }
        fun intRGB(color: Color): Int {
            val r = (color.r * 255.999).toInt()
            val g = (color.g * 255.999).toInt()
            val b = (color.b * 255.999).toInt()
            return r.shl(16) + g.shl(8) + b
        }
    }
}