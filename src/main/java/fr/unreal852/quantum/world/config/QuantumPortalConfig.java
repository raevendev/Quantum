package fr.unreal852.quantum.world.config;

import com.google.gson.annotations.Expose;
import net.minecraft.util.Identifier;

public class QuantumPortalConfig {
    @Expose
    private boolean enabled = true;
    @Expose
    private Identifier destinationId;
    @Expose
    private Identifier portalBlockId;
    @Expose
    private Identifier portalIgniteItemId;
    @Expose
    private int portalColor;

    public QuantumPortalConfig() {
    }

    public QuantumPortalConfig(Identifier destId, Identifier portalBlockId, Identifier portalIgniteItemId, int color) {
        this.destinationId = destId;
        this.portalBlockId = portalBlockId;
        this.portalIgniteItemId = portalIgniteItemId;
        this.portalColor = color;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Identifier getDestinationId() {
        return this.destinationId;
    }

    public Identifier getPortalBlockId() {
        return this.portalBlockId;
    }

    public Identifier getPortalIgniteItemId() {
        return this.portalIgniteItemId;
    }

    public int getPortalColor() {
        return this.portalColor;
    }
}
