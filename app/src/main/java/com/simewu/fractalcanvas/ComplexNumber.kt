package com.simewu.fractalcanvas


class ComplexNumber(real: Double, complex: Double) {
    var real = real
    var imag = complex

    fun clone(): ComplexNumber {
        return ComplexNumber(real, imag)
    }

    fun add(other: ComplexNumber): ComplexNumber {
        return ComplexNumber(
            real + other.real,
            imag + other.imag
        )
    }
    fun add(num: Double): ComplexNumber {
        return ComplexNumber(
            real + num,
            imag
        )
    }
    fun sub(other: ComplexNumber): ComplexNumber {
        return ComplexNumber(
            real - other.real,
            imag - other.imag
        )
    }
    fun sub(num: Double): ComplexNumber {
        return ComplexNumber(
            real - num,
            imag
        )
    }
    fun mul(other: ComplexNumber): ComplexNumber {
        val real_ = real * other.real - imag * other.imag
        val imag_ = imag * other.real + real * other.imag
        return ComplexNumber(
            real_,
            imag_
        )
    }
    fun mul(num: Double): ComplexNumber {
        return ComplexNumber(
            real * num,
            imag * num
        )
    }
    fun div(other: ComplexNumber): ComplexNumber {
        var r = (other.real * other.real + other.imag * other.imag)
        if(r == 0.0) return ComplexNumber(0.0,0.0)
        val real_ = (real * other.real + imag * other.imag) / r
        val imag_ = (imag * other.real - real * other.imag) / r
        return ComplexNumber(
            real_,
            imag_
        )
    }
    fun div(num: Double): ComplexNumber {
        if(num == 0.0) return ComplexNumber(0.0,0.0)
        val real_ = real / num
        val imag_ = imag / num
        return ComplexNumber(
            real_,
            imag_
        )
    }
    fun norm(): ComplexNumber {
        return ComplexNumber(
            real * real + imag * imag,
            0.0
        )
    }
    fun abs(): ComplexNumber {
        return ComplexNumber(
            Math.sqrt(real * real + imag * imag),
            0.0
        )
    }
    fun neg(): ComplexNumber {
        return ComplexNumber(
            -real,
            -imag
        )
    }
    fun exp(): ComplexNumber {
        val e = Math.exp(real)
        return ComplexNumber(
            Math.cos(imag) * e,
            Math.sin(imag) * e
        )
    }
    fun theta(): Double {
        return Math.atan2(imag, real)
        /*var a = Math.atan(imag / real)
        if(real < 0) a += Math.PI
        return a*/
    }
    fun log(): ComplexNumber {
        return ComplexNumber(
            Math.log(Math.sqrt(real * real + imag * imag)),
            theta()
        )
    }
    fun inv(): ComplexNumber {
        var norm = real * real + imag * imag
        if(norm == 0.0) return ComplexNumber(0.0,0.0)
        return ComplexNumber(real / norm, -imag / norm)
    }
    fun pow(power: ComplexNumber): ComplexNumber {
        var r = Math.sqrt(real * real + imag * imag)
        var theta = theta()
        return ComplexNumber(Math.pow(r, real) * Math.pow(Math.E, -imag * theta), 0.0).mul(ComplexNumber(Math.cos(imag * Math.log(r) + real * theta), Math.sin(imag * Math.log(r) + real * theta)))
        //return log().mul(power).exp()
    }
    fun pow(power: Double): ComplexNumber {
        return pow(ComplexNumber(power, 0.0))
        /*
        var inverse = false
        var p = power
        if(power < 0) {
            inverse = true
            p = -p
        }
        var result = ComplexNumber(1.0, 0.0)
        var multiplier = ComplexNumber(real, imag)
        while(p > 0) {
            if ((power % 2) != 0) result = result.mul(multiplier)
            multiplier.mul(multiplier)
            p /= 2
        }
        if(inverse) return result.inv()
        else return result*/
    }
    fun sqrt(): ComplexNumber {
        return log().mul(0.5).exp()
    }
    fun sin(): ComplexNumber {
        var i = ComplexNumber(0.0, 1.0)
        return i.mul(this).sinh().div(i)
    }
    fun cos(): ComplexNumber {
        var i = ComplexNumber(0.0, 1.0)
        return i.mul(this).cosh()
    }
    fun sinh(): ComplexNumber {
        return exp().sub(this.neg().exp()).mul(0.5)
    }
    fun cosh(): ComplexNumber {
        return exp().add(this.neg().exp()).mul(0.5)
    }
    fun equal(other: ComplexNumber): Boolean {
        return (real == other.real) && (imag == other.imag)
    }
    fun dist(): Double {
        return real * real + imag * imag
    }


}