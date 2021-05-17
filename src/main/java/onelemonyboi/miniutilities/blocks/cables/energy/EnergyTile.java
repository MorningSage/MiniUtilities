package onelemonyboi.miniutilities.blocks.cables.energy;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import onelemonyboi.lemonlib.blocks.EnergyTileBase;
import onelemonyboi.miniutilities.blocks.cables.MUCableSide;
import onelemonyboi. miniutilities.init.TEList;

import java.util.HashMap;
import java.util.Map;

import static onelemonyboi.miniutilities.blocks.cables.energy.EnergyBlock.*;

public class EnergyTile extends EnergyTileBase {
    public EnergyTile() {
        super(TEList.EnergyTile.get(), 8192, 8192, 8192);
    }

    @Override
    public void tick() {
        powerIO();
    }

    public void powerIO() {
        powerLogic(UP, Direction.UP);
        powerLogic(DOWN, Direction.DOWN);
        powerLogic(NORTH, Direction.NORTH);
        powerLogic(EAST, Direction.EAST);
        powerLogic(SOUTH, Direction.SOUTH);
        powerLogic(WEST, Direction.WEST);
    }

    public void powerLogic(EnumProperty<MUCableSide> prop, Direction d) {
        if (getBlockState().get(prop) == MUCableSide.PULL) {
            energy.inputFromSide(world, pos, d, 8192);
        }
        else if (getBlockState().get(prop) == MUCableSide.PUSH) {
            energy.outputToSide(world, pos, d, 8192);
        }
    }

    public CompoundNBT write(CompoundNBT nbt) {
        this.energy.write(nbt);
        return super.write(nbt);
    }

    public void read(BlockState state, CompoundNBT nbt) {
        this.energy.read(nbt);
        super.read(state, nbt);
    }
}
