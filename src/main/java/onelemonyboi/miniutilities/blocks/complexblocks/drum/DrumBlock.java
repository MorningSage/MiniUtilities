package onelemonyboi.miniutilities.blocks.complexblocks.drum;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DrumBlock extends ContainerBlock {
    public final int mb;

    public DrumBlock(int mb, Properties properties) {
        super(properties);
        this.mb = mb;
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable IBlockReader blockReader, @Nonnull List<ITextComponent> textComponents, @Nonnull ITooltipFlag tooltipFlag) {
        CompoundNBT nbt = stack.getChildTag("BlockEntityTag");

        ITextComponent fluidName = ITextComponent.getTextComponentOrEmpty("Empty");
        int fluidAmount = 0;

        if (nbt != null) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
            fluidName = fluidStack.getFluid().getAttributes().getDisplayName(fluidStack);
            fluidAmount = fluidStack.getAmount();
        }

        textComponents.add(new TranslationTextComponent("text.miniutilities.tooltip.drum_fluid", fluidName));
        textComponents.add(new TranslationTextComponent("text.miniutilities.tooltip.drum_amount", fluidAmount, mb));

        super.addInformation(stack, blockReader, textComponents, tooltipFlag);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack held = player.getHeldItem(hand);

        if (FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace()) ||
                held.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new DrumTile(this.mb);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.isRemote()) {
            return;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        ItemStack itemStack = new ItemStack(this);
        CompoundNBT compoundNBT = tileEntity.write(new CompoundNBT());
        itemStack.setTagInfo("BlockEntityTag", compoundNBT);
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemStack);
    }
}
