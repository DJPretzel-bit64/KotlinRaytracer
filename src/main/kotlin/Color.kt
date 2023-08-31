class Color(r: Double = 0.0, g: Double = 0.0, b: Double = 0.0) : Vec3(r, g, b) {
    companion object {
        fun writeColor(pixelColor: Color) {
            println("${(255.999 * pixelColor.x).toInt()} ${(255.999 * pixelColor.y).toInt()} ${(255.999 * pixelColor.z).toInt()}")
        }
    }
}