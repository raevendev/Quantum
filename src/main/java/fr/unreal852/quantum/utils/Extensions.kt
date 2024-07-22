package fr.unreal852.quantum.utils

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier

object Extensions {
    fun NbtCompound.getIdentifier(key: String): Identifier {
        return Identifier.of(this.getString(key))
    }
}