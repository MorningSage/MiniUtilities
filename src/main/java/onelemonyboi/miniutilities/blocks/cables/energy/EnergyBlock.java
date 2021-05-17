package onelemonyboi.miniutilities.blocks.cables.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import onelemonyboi.miniutilities.blocks.cables.MUCableSide;
import onelemonyboi.miniutilities.init.TEList;

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
    public static Map<Direction, EnumProperty<MUCableSide>> map = new HashMap<>();

    public EnergyBlock(Properties properties) {
        super(properties);
        this.setDefaultState(getDefaultState()
                .with(UP, NONE)
                .with(DOWN, NONE)
                .with(NORTH, NONE)
                .with(EAST, NONE)
                .with(SOUTH, NONE)
                .with(WEST, NONE));
        map.put(Direction.UP, UP);
        map.put(Direction.DOWN, DOWN);
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        sideCheck(worldIn, pos, state);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    static VoxelShape BASE = Block.makeCuboidShape(6, 6, 6, 10, 10, 10);
    static VoxelShape NORTHSHAPE = Block.makeCuboidShape(6, 6, 0, 10, 10, 6);
    static VoxelShape SOUTHSHAPE = Block.makeCuboidShape(10, 6, 6, 16, 10, 10);
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
            worldIn.addParticle(ParticleTypes.SMOKE, hit.getHitVec().x, hit.getHitVec().y, hit.getHitVec().z, 0, 0, 0);
            return ActionResultType.CONSUME;
        }

        System.out.println(state.get(map.get(hit.getFace())));

        if (state.get(map.get(hit.getFace())) == PUSH) {
            worldIn.setBlockState(pos, state.with(map.get(hit.getFace()), PULL));
        } else if (state.get(map.get(hit.getFace())) == PULL) {
            worldIn.setBlockState(pos, state.with(map.get(hit.getFace()), DISABLED));
        } else if (state.get(map.get(hit.getFace())).isDisconnected()) {
            worldIn.setBlockState(pos, state.with(map.get(hit.getFace()), PUSH));
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
}
