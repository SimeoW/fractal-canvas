package com.simewu.fractalcanvas

class Fractal {
    var xoff = 1.toDouble()
    var yoff = 1.toDouble()
    var scale = 1.toDouble()
    var iterations = 100
    var breakOut = 4
    var equation = "Z^2 + C"
    var resolution = 20
    var detail = 1

    fun clone(): Fractal {
        val clone = Fractal()
        clone.xoff = xoff
        clone.yoff = yoff
        clone.scale = scale
        clone.iterations = iterations
        clone.breakOut = breakOut
        clone.equation = equation
        clone.resolution = resolution
        clone.detail = detail
        return clone
    }

    fun generateLambda(expression: String) {

    }
    fun eval(x: Double, y: Double): Int {
        //return (x * y).toInt()
        var i = 0
        var c = ComplexNumber(x, y)
        var z = ComplexNumber(0.0, 0.0)
        var breakOut2 = breakOut * breakOut
        for (i in 1..iterations) {
            if(z.dist() >= breakOut2) {
                return i
            }
            //z = z.mul(z).add(c)
            //z = z.abs()
            z = z.mul(z).add(c)
        }
        return iterations * detail
    }
}