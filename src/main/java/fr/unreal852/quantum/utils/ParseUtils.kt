package fr.unreal852.quantum.utils

object ParseUtils {
    @JvmStatic
    fun tryParseLong(value: String, defaultVal: Int): Long {
        return try {
            value.toLong()
        } catch (e: NumberFormatException) {
            defaultVal.toLong()
        }
    }
}
