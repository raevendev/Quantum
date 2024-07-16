package fr.unreal852.quantum.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public final class TextUtils {
    public static Text literal(String message, Formatting formatting) {
        MutableText mutableText = Text.literal(message);
        Style textStyle = mutableText.getStyle().withFormatting(formatting);
        mutableText.setStyle(textStyle);
        return mutableText;
    }

    public static Text literal(String message, int rgbColor) {
        var mutableText = Text.literal(message);
        var textStyle = mutableText.getStyle().withColor(TextColor.fromRgb(rgbColor));
        mutableText.setStyle(textStyle);
        return mutableText;
    }

    public static Text literal(String message, TextColor textColor) {
        return literal(message, textColor.getRgb());
    }
}