package rationals

import java.math.BigInteger

fun String.toRational(): Rational {
    return if ("/" in this) {
        val (num, dom) = this.split("/")
        Rational(num.toBigInteger(), dom.toBigInteger())
    } else {
        Rational(this.toBigInteger(), BigInteger.ONE)
    }
}


infix fun Number.divBy(i: Number): Rational {
    return Rational(this.toLong().toBigInteger(), i.toLong().toBigInteger())
}

data class Rational(var num: BigInteger, var dom: BigInteger) : Comparable<Rational> {

    init {
        val gcd = num.gcd(dom)
        if (dom < BigInteger.ZERO) {
            num *= -BigInteger.ONE
            dom *= -BigInteger.ONE
        }

        num /= gcd
        dom /= gcd
    }

    operator fun plus(other: Rational): Rational  {
        val num1 = num * other.dom
        val num2 = dom * other.num

        return Rational(num1 + num2, dom * other.dom)
    }

    operator fun minus(other: Rational): Rational {
        val num1 = num * other.dom
        val num2 = dom * other.num

        return Rational(num1 - num2, dom * other.dom)
    }

    operator fun times(other: Rational) = Rational(num * other.num, dom * other.dom)

    operator fun div(other: Rational) = Rational(num * other.dom, dom * other.num)

    operator fun unaryMinus() = Rational(-num, dom)

    override operator fun compareTo(other: Rational): Int {
        return (num * other.dom).compareTo(other.num * dom)
    }

    operator fun rangeTo(other: Rational): ClosedRange<Rational> {
        return object: ClosedRange<Rational> {
            override val endInclusive: Rational = other
            override val start: Rational =this@Rational
        }

    }

    operator fun contains(other:Rational): Boolean {
        return true
    }

    override fun toString(): String {
        return if(dom == BigInteger.ONE) "$num" else "${num}/${dom}"
    }

}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)

    println()
}
