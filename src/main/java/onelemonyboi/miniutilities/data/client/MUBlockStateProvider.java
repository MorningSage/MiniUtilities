package onelemonyboi.miniutilities.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import onelemonyboi.miniutilities.MiniUtilities;
import onelemonyboi.miniutilities.blocks.cables.MUCableSide;
import onelemonyboi.miniutilities.blocks.cables.energy.EnergyBlock;
import onelemonyboi.miniutilities.init.BlockList;

public class MUBlockStateProvider extends BlockStateProvider {
    public MUBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MiniUtilities.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(BlockList.EnderOre.get());
        simpleBlock(BlockList.EnderPearlBlock.get());
        simpleBlock(BlockList.AngelBlock.get());

        // Lapis Caelestis
        simpleBlock(BlockList.WhiteLapisCaelestis.get());
        simpleBlock(BlockList.LightGrayLapisCaelestis.get());
        simpleBlock(BlockList.GrayLapisCaelestis.get());
        simpleBlock(BlockList.BlackLapisCaelestis.get());
        simpleBlock(BlockList.RedLapisCaelestis.get());
        simpleBlock(BlockList.OrangeLapisCaelestis.get());
        simpleBlock(BlockList.YellowLapisCaelestis.get());
        simpleBlock(BlockList.LimeLapisCaelestis.get());
        simpleBlock(BlockList.GreenLapisCaelestis.get());
        simpleBlock(BlockList.LightBlueLapisCaelestis.get());
        simpleBlock(BlockList.CyanLapisCaelestis.get());
        simpleBlock(BlockList.BlueLapisCaelestis.get());
        simpleBlock(BlockList.PurpleLapisCaelestis.get());
        simpleBlock(BlockList.MagentaLapisCaelestis.get());
        simpleBlock(BlockList.PinkLapisCaelestis.get());
        simpleBlock(BlockList.BrownLapisCaelestis.get());

        registerEnergyCableState(BlockList.RedstoneCabling.get());
    }

    public void registerEnergyCableState(Block block) {
        String centerName = block.getRegistryName().getPath() + "_center";
        ModelFile.ExistingModelFile center = models().getExistingFile(modLoc(centerName));

        String segmentName = block.getRegistryName().getPath() + "_segment";
        ModelFile.ExistingModelFile segment = models().getExistingFile(modLoc(segmentName));

        MultiPartBlockStateBuilder multipart = getMultipartBuilder(block);
        multipart.part().modelFile(center).addModel();
        multipart.part().modelFile(segment).uvLock(true).addModel().condition(EnergyBlock.SOUTH, MUCableSide.PULL, MUCableSide.PUSH, MUCableSide.BASE);
        multipart.part().modelFile(segment).uvLock(true).rotationY(90).addModel().condition(EnergyBlock.WEST, MUCableSide.PULL, MUCableSide.PUSH, MUCableSide.BASE);
        multipart.part().modelFile(segment).uvLock(true).rotationY(180).addModel().condition(EnergyBlock.NORTH, MUCableSide.PULL, MUCableSide.PUSH, MUCableSide.BASE);
        multipart.part().modelFile(segment).uvLock(true).rotationY(270).addModel().condition(EnergyBlock.EAST, MUCableSide.PULL, MUCableSide.PUSH, MUCableSide.BASE);
        multipart.part().modelFile(segment).uvLock(true).rotationX(90).addModel().condition(EnergyBlock.UP, MUCableSide.PULL, MUCableSide.PUSH, MUCableSide.BASE);
        multipart.part().modelFile(segment).uvLock(true).rotationX(270).addModel().condition(EnergyBlock.DOWN, MUCableSide.PULL, MUCableSide.PUSH, MUCableSide.BASE);
    }
}
