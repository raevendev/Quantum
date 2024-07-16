package fr.unreal852.quantum.utils;

public final class ParseUtils
{
    public static long tryParseLong(String value, int defaultVal)
    {
        try
        {
            return Long.parseLong(value);
        }
        catch (NumberFormatException e)
        {
            return defaultVal;
        }
    }
}
