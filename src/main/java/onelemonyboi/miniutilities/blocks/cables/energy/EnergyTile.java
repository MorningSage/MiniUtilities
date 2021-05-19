package onelemonyboi.miniutilities.blocks.cables.energy;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import onelemonyboi.lemonlib.blocks.EnergyTileBase;
import onelemonyboi.miniutilities.blocks.cables.MUCableSide;
import onelemonyboi. miniutilities.init.TEList;

import java.util.Map;

import static onelemonyboi.miniutilities.blocks.cables.energy.EnergyBlock.*;

public class EnergyTile extends EnergyTileBase {
    public EnergyTile() {
        super(TEList.EnergyTile.get(), 8192, 64, 64);
    }

    @Override
    public void tick() {
        InputOutput();
    }

    public void InputOutput() {
        for (Map.Entry<Direction, EnumProperty<MUCableSide>> entry : directionMap.entrySet()) {
            IOLogic(entry.getValue(), entry.getKey());
        }
    }

    // TODO: FIX IO HOLY SHIT ITS BAD HELP PLS

    public void IOLogic(EnumProperty<MUCableSide> prop, Direction d) {
        if (getBlockState().get(prop) == MUCableSide.PULL) {
            energy.inputFromSide(world, pos, d, 64);
        }
        else if (getBlockState().get(prop) == MUCableSide.PUSH) {
            energy.outputToSide(world, pos, d, 64);
        }
    }
}
