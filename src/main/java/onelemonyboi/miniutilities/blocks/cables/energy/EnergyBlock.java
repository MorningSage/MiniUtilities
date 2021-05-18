package onelemonyboi.miniutilities.blocks.cables.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import onelemonyboi.miniutilities.MiniUtilities;
import onelemonyboi.miniutilities.blocks.cables.MUCableSide;
import onelemonyboi.miniutilities.init.TEList;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static onelemonyboi.miniutilities.blocks.cables.MUCableSide.*;

public class EnergyBlock extends Block {
    public static final EnumProperty<MUCableSide> UP = EnumProperty.create("up", MUCableSide.class);
    public static final EnumProperty<MUCableSide> DOWN = EnumProperty.create("down", MUCableSide.class);
    public static final EnumProperty<MUCableSide> NORTH = EnumProperty.create("north", MUCableSide.class);
    public static final EnumProperty<MUCableSide> EAST = EnumProperty.create("east", MUCableSide.class);
    public static final EnumProperty<MUCableSide> SOUTH = EnumProperty.create("south", MUCableSide.class);
    public static final EnumProperty<MUCableSide> WEST = EnumProperty.create("west", MUCableSide.class);
    public static Map<Direction, EnumProperty<MUCableSide>> directionMap = new HashMap<>();

    public EnergyBlock(Properties properties) {
        super(properties);
        this.setDefaultState(getDefaultState()
                .with(UP, NONE)
                .with(DOWN, NONE)
                .with(NORTH, NONE)
                .with(EAST, NONE)
                .with(SOUTH, NONE)
                .with(WEST, NONE));
        directionMap.put(Direction.UP, UP);
        directionMap.put(Direction.DOWN, DOWN);
        directionMap.put(Direction.NORTH, NORTH);
        directionMap.put(Direction.EAST, EAST);
        directionMap.put(Direction.SOUTH, SOUTH);
        directionMap.put(Direction.WEST, WEST);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        sideCheck(worldIn, pos, state);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    static VoxelShape BASE = Block.makeCuboidShape(6, 6, 6, 10, 10, 10);
    static VoxelShape NORTHSHAPE = Block.makeCuboidShape(6, 6, 0, 10, 10, 6);
    static VoxelShape SOUTHSHAPE = Block.makeCuboidShape(6, 6, 10, 10, 10, 16);
    static VoxelShape EASTSHAPE = Block.makeCuboidShape(10, 6, 6, 16, 10, 10);
    static VoxelShape WESTSHAPE = Block.makeCuboidShape(0, 6, 6, 6, 10, 10);
    static VoxelShape DOWNSHAPE = Block.makeCuboidShape(6, 0, 6, 10, 6, 10);
    static VoxelShape UPSHAPE = Block.makeCuboidShape(6, 10, 6, 10, 16, 10);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape returned = BASE;

        if (state.get(UP).isConnected()) {returned = VoxelShapes.combineAndSimplify(returned, UPSHAPE, IBooleanFunction.OR);}
        if (state.get(NORTH).isConnected()) {returned = VoxelShapes.combineAndSimplify(returned, NORTHSHAPE, IBooleanFunction.OR);}
        if (state.get(SOUTH).isConnected()) {returned = VoxelShapes.combineAndSimplify(returned, SOUTHSHAPE, IBooleanFunction.OR);}
        if (state.get(WEST).isConnected()) {returned = VoxelShapes.combineAndSimplify(returned, WESTSHAPE, IBooleanFunction.OR);}
        if (state.get(EAST).isConnected()) {returned = VoxelShapes.combineAndSimplify(returned, EASTSHAPE, IBooleanFunction.OR);}
        if (state.get(DOWN).isConnected()) {returned = VoxelShapes.combineAndSimplify(returned, DOWNSHAPE, IBooleanFunction.OR);}

        return returned;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TEList.EnergyTile.get().create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UP);
        builder.add(DOWN);
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.CONSUME;
        }

        Triple<Long, Long, Long> triple = getRelativePositionPixels(pos, hit);

        // Why is this so long, probably can be simplified, but too lazy

        if (isBetween(6, 10, triple.getLeft()) && isBetween(6, 10, triple.getMiddle()) && isBetween(0, 6, triple.getRight())) {
            changeSideMode(state, Direction.NORTH, worldIn, pos);
        }
        else if (isBetween(6, 10, triple.getLeft()) && isBetween(6, 10, triple.getMiddle()) && isBetween(10, 16, triple.getRight())) {
            changeSideMode(state, Direction.SOUTH, worldIn, pos);
        }
        else if (isBetween(10, 16, triple.getLeft()) && isBetween(6, 10, triple.getMiddle()) && isBetween(6, 10, triple.getRight())) {
            changeSideMode(state, Direction.EAST, worldIn, pos);
        }
        else if (isBetween(0, 6, triple.getLeft()) && isBetween(6, 10, triple.getMiddle()) && isBetween(6, 10, triple.getRight())) {
            changeSideMode(state, Direction.WEST, worldIn, pos);
        }
        else if (isBetween(6, 10, triple.getLeft()) && isBetween(0, 6, triple.getMiddle()) && isBetween(6, 10, triple.getRight())) {
            changeSideMode(state, Direction.DOWN, worldIn, pos);
        }
        else {
            changeSideMode(state, Direction.UP, worldIn, pos);
        }
        return ActionResultType.CONSUME;
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
            world.setBlockState(pos, state.with(prop, MUCableSide.NONE));
            return;
        }
        LazyOptional<IEnergyStorage> opt = world.getTileEntity(pos.offset(d)).getCapability(CapabilityEnergy.ENERGY, d.getOpposite());
        IEnergyStorage storage = opt.orElse(null);
        if (state.get(prop).canEnable() && storage != null) {
            world.setBlockState(pos, state.with(prop, MUCableSide.PUSH));
        }
    }

    public static Triple<Double, Double, Double> getRelativePosition(BlockPos pos, BlockRayTraceResult hit) {
        Vector3d vector = hit.getHitVec();
        Triple<Double, Double, Double> triple = Triple.of(vector.getX() - pos.getX(), vector.getY() - pos.getY(), vector.getZ() - pos.getZ());
        return triple;
    }

    public static Triple<Long, Long, Long> getRelativePositionPixels(BlockPos pos, BlockRayTraceResult hit) {
        Triple<Double, Double, Double> triple = getRelativePosition(pos, hit);
        Triple<Long, Long, Long> exitTriple = Triple.of(Math.round(triple.getLeft() * 16), Math.round(triple.getMiddle() * 16), Math.round(triple.getRight() * 16));
        return exitTriple;
    }

    public static boolean isBetween(long low, long high, long num) {
        return num >= low && num <= high;
    }

    public void changeSideMode(BlockState state, Direction d, World world, BlockPos pos) {
        if (state.get(directionMap.get(d)) == PUSH) {
            world.setBlockState(pos, state.with(directionMap.get(d), PULL));
        } else if (state.get(directionMap.get(d)) == PULL) {
            world.setBlockState(pos, state.with(directionMap.get(d), DISABLED));
        } else if (state.get(directionMap.get(d)).isDisconnected()) {
            world.setBlockState(pos, state.with(directionMap.get(d), PUSH));
        }
    }
}
