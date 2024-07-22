package fr.unreal852.quantum.utils

import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting

object TextUtils {
    fun literal(message: String, formatting: Formatting?): Text {
        val mutableText = Text.literal(message)
        val textStyle = mutableText.style.withFormatting(formatting)
        mutableText.setStyle(textStyle)
        return mutableText
    }

    fun literal(message: String, rgbColor: Int): Text {
        val mutableText = Text.literal(message)
        val textStyle = mutableText.style.withColor(TextColor.fromRgb(rgbColor))
        mutableText.setStyle(textStyle)
        return mutableText
    }

    fun literal(message: String, textColor: TextColor): Text {
        return literal(message, textColor.rgb)
    }
}