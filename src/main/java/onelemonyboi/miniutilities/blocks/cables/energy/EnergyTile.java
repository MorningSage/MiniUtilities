package onelemonyboi.miniutilities.blocks.cables.energy;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import onelemonyboi.lemonlib.blocks.EnergyTileBase;
import onelemonyboi.miniutilities.MiniUtilities;
import onelemonyboi.miniutilities.blocks.cables.MUCableSide;
import onelemonyboi. miniutilities.init.TEList;

import java.util.Map;

import static onelemonyboi.miniutilities.blocks.cables.MUCableSide.DISABLED;
import static onelemonyboi.miniutilities.blocks.cables.energy.EnergyBlock.*;

public class EnergyTile extends EnergyTileBase {
    private int recieveAndExtract;
    public EnergyTile() {
        super(TEList.EnergyTile.get(), 8192, 640, 640);
        recieveAndExtract = 640;
    }

    @Override
    public void tick() {
        InputOutput();
        sideCheck(world, pos, getBlockState());
    }

    public void InputOutput() {
        for (Map.Entry<Direction, EnumProperty<MUCableSide>> entry : directionMap.entrySet()) {
            IOLogic(entry.getValue(), entry.getKey());
        }
    }

    public void IOLogic(EnumProperty<MUCableSide> prop, Direction d) {
        if (getBlockState().get(prop) == MUCableSide.PULL) {
            energy.inputFromSide(world, pos, d, recieveAndExtract);
        }
        else if (getBlockState().get(prop) == MUCableSide.PUSH) {
            energy.outputToSide(world, pos, d, recieveAndExtract);
        }
    }

    public void sideCheck(World worldIn, BlockPos pos, BlockState state) {
        enableSide(UP, Direction.UP, worldIn, pos, state);
        enableSide(DOWN, Direction.DOWN, worldIn, pos, state);
        enableSide(NORTH, Direction.NORTH, worldIn, pos, state);
        enableSide(EAST, Direction.EAST, worldIn, pos, state);
        enableSide(SOUTH, Direction.SOUTH, worldIn, pos, state);
        enableSide(WEST, Direction.WEST, worldIn, pos, state);
    }

    public void enableSide(EnumProperty<MUCableSide> prop, Direction d, World world, BlockPos pos, BlockState state) {
        if (world.getTileEntity(pos.offset(d)) == null) {
            return;
        }

        LazyOptional<IEnergyStorage> opt = world.getTileEntity(pos.offset(d)).getCapability(CapabilityEnergy.ENERGY, d.getOpposite());
        if (opt != null) {
            world.setBlockState(pos, state.with(prop, MUCableSide.BASE));
        }
    }
}
