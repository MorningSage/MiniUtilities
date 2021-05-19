package onelemonyboi.miniutilities.blocks.cables;

import net.minecraft.util.IStringSerializable;

public enum MUCableSide implements IStringSerializable {
    PUSH("push"),
    PULL("pull"),
    DISABLED("disabled"),
    BASE("base");

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
        return !this.isDisconnected();
    }
    public boolean isDisconnected() {
        return this == DISABLED;
    }
}
