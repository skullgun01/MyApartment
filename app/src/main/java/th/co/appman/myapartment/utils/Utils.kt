package th.co.appman.myapartment.utils

import java.util.*

fun randomNumber(): Int {
    val random = Random()
    return random.nextInt(10)
}