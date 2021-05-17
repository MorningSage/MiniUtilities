package onelemonyboi.miniutilities.blocks.cables;

import net.minecraft.util.IStringSerializable;

public enum MUCableSide implements IStringSerializable {
    PUSH("push"),
    PULL("pull"),
    DISABLED("disabled"),
    NONE("none");

    private final String name;

    private MUCableSide(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getString();
    }

    public String getString() {
        return this.name;
    }

    public boolean isConnected() {
        return this == PUSH || this == PULL;
    }
    public boolean isDisconnected() {
        return this == DISABLED || this == NONE;
    }
    public boolean isDisabled() {
        return this == DISABLED;
    }
    public boolean canEnable() {
        return this == NONE;
    }
}
