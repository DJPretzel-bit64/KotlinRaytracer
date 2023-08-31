fun main() {
    val imageWidth = 256
    val imageHeight = 256

    print("P3\n$imageWidth $imageHeight\n255\n")

    for(j in 0..<imageHeight) {
        for(i in 0..<imageWidth) {
            val pixelColor = Color(i / (imageWidth -1).toDouble(), j / (imageHeight - 1).toDouble(), 0.0)
            Color.writeColor(pixelColor)
        }
    }
}