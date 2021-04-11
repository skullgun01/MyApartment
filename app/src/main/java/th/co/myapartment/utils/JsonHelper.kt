package th.co.myapartment.utils

import com.google.gson.*
import com.google.gson.reflect.TypeToken

object JsonHelper {
    private val gson = Gson()

    fun <T : Any?> toModel(src: Any, className: Class<T>): T {
        return gson.fromJson(src.toString(), className)
    }

    inline fun <reified T> toType(json: String): T {
        return Gson().fromJson<T>(json, object : TypeToken<T>() {}.type)
    }
}
