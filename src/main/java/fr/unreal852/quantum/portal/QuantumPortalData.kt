package fr.unreal852.quantum.portal

import fr.unreal852.quantum.utils.Extensions.getIdentifier
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier

class QuantumPortalData {

    lateinit var destinationId: Identifier
        private set

    lateinit var portalBlockId: Identifier
        private set

    lateinit var portalIgniteItemId: Identifier
        private set

    var portalColor: Int = 0
        private set

    fun writeToNbt(nbt: NbtCompound) {
        nbt.putString(DESTINATION_KEY, destinationId.toString())
        nbt.putString(BLOCK_KEY, portalBlockId.toString())
        nbt.putString(IGNITE_KEY, portalIgniteItemId.toString())
        nbt.putInt(COLOR_KEY, portalColor)
    }

    constructor()

    constructor(destId: Identifier, portalBlockId: Identifier, portalIgniteItemId: Identifier, color: Int) {
        this.destinationId = destId
        this.portalBlockId = portalBlockId
        this.portalIgniteItemId = portalIgniteItemId
        this.portalColor = color
    }

    companion object {

        private const val DESTINATION_KEY = "destinationId"
        private const val BLOCK_KEY = "blockId"
        private const val IGNITE_KEY = "igniteId"
        private const val COLOR_KEY = "color"

        fun fromNbt(nbt: NbtCompound): QuantumPortalData {

            val destinationId = nbt.getIdentifier(DESTINATION_KEY)
            val blockId = nbt.getIdentifier(BLOCK_KEY)
            val igniteId = nbt.getIdentifier(IGNITE_KEY)
            val color = nbt.getInt(COLOR_KEY)

            val quantumPortalData = QuantumPortalData(
                destinationId,
                blockId,
                igniteId,
                color
            )
            return quantumPortalData
        }
    }
}
