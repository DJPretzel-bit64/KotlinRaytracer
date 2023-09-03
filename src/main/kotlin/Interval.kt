class Interval(val min: Double, val max: Double) {
    fun contains(x: Double) = x in min..max

    fun surrounds(x: Double) = min < x && x < max

    fun clamp(x: Double): Double {
        if(x < min) return min
        if(x > max) return max
        return x
    }
}