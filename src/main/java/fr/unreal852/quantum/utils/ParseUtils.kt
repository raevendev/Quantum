package fr.unreal852.quantum.utils

object ParseUtils {
    @JvmStatic
    fun tryParseLong(value: String, defaultVal: Long): Long {
        return try {
            value.toLong()
        } catch (e: NumberFormatException) {
            defaultVal
        }
    }
}
