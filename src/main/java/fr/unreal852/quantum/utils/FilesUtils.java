package fr.unreal852.quantum.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.unreal852.quantum.Quantum;
import net.minecraft.util.Identifier;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FilesUtils
{
    public static final Gson GSON;

    public static File[] listFiles(File file)
    {
        File[] files = file.listFiles();
        return files == null ? new File[0] : files;
    }

    public static <T> T readObjectFromJsonFile(Class<T> clazz, File file)
    {
        try
        {
            return !file.exists() ? null : GSON.fromJson(Files.readString(file.toPath()), clazz);
        }
        catch (Exception var3)
        {
            Quantum.LOGGER.error(var3.getMessage());
            return null;
        }
    }

    public static <T> void writeObjectToJsonFile(File file, T object)
    {
        writeObjectToJsonFile(file.toPath(), object);
    }

    public static <T> void writeObjectToJsonFile(Path filePath, T object)
    {
        try
        {
            if (!Files.exists(filePath.getParent()) || !Files.exists(filePath))
            {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }

            Files.writeString(filePath, GSON.toJson(object, object.getClass()));
        }
        catch (Exception var3)
        {
            Quantum.LOGGER.error(var3.getMessage());
        }

    }

    static
    {
        GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Identifier.class, new Identifier.Serializer()).create();
    }
}