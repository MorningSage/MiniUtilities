package onelemonyboi.miniutilities.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import onelemonyboi.miniutilities.MiniUtilities;
import onelemonyboi.miniutilities.init.BlockList;

public class BlockState extends BlockStateProvider {
    public BlockState(DataGenerator gen, ExistingFileHelper exFileHelper) {
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

        // Special Glass
        simpleBlock(BlockList.EtherealGlass.get());
        simpleBlock(BlockList.ReverseEtherealGlass.get());
        simpleBlock(BlockList.RedstoneGlass.get());
        simpleBlock(BlockList.GlowingGlass.get());
        simpleBlock(BlockList.DarkGlass.get());
        simpleBlock(BlockList.DarkEtherealGlass.get());
        simpleBlock(BlockList.DarkReverseEtherealGlass.get());

        simpleBlock(BlockList.LapisLamp.get());
    }
}
