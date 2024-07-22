package fr.unreal852.quantum.world.config

import com.google.gson.annotations.Expose
import net.minecraft.util.Identifier

class QuantumPortalConfig {
    @Expose
    val isEnabled: Boolean = true

    @Expose
    var destinationId: Identifier? = null
        private set

    @Expose
    var portalBlockId: Identifier? = null
        private set

    @Expose
    var portalIgniteItemId: Identifier? = null
        private set

    @Expose
    var portalColor: Int = 0
        private set

    constructor()

    constructor(destId: Identifier?, portalBlockId: Identifier?, portalIgniteItemId: Identifier?, color: Int) {
        this.destinationId = destId
        this.portalBlockId = portalBlockId
        this.portalIgniteItemId = portalIgniteItemId
        this.portalColor = color
    }
}
