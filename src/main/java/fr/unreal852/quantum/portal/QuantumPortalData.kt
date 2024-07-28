package fr.unreal852.quantum.portal

import net.minecraft.util.Identifier

class QuantumPortalData {

    val isEnabled: Boolean = true

    lateinit var destinationId: Identifier
        private set

    lateinit var portalBlockId: Identifier
        private set

    lateinit var portalIgniteItemId: Identifier
        private set

    var portalColor: Int = 0
        private set

    constructor()

    constructor(destId: Identifier, portalBlockId: Identifier, portalIgniteItemId: Identifier, color: Int) {
        this.destinationId = destId
        this.portalBlockId = portalBlockId
        this.portalIgniteItemId = portalIgniteItemId
        this.portalColor = color
    }
}
