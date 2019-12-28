package com.tfar.nametagswithoutanvil;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NametagsWithoutAnvil.MODID)
public class NametagsWithoutAnvil
{
  // Directly reference a log4j logger.

  public static final String MODID = "nametagswithoutanvil";
  public static final Tag<Item> quill = new ItemTags.Wrapper(new ResourceLocation(MODID, "quill"));
  private static final Logger LOGGER = LogManager.getLogger();

  public static final CommonConfig COMMON;
  public static final ForgeConfigSpec COMMON_SPEC;

  static {
    final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    COMMON_SPEC = specPair.getRight();
    COMMON = specPair.getLeft();
  }

  public NametagsWithoutAnvil() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
  }

  private void setup(final FMLCommonSetupEvent event) {
    PacketHandler.registerMessages(MODID);
  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    ScreenManager.registerFactory(NAMETAG_CONTAINER, NametagScreen::new);
  }

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e) {
      e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new NameTagContainer(windowId, inv)).setRegistryName("nametag_container"));
    }
  }

  @ObjectHolder("nametagswithoutanvil:nametag_container")
  public static final ContainerType<NameTagContainer> NAMETAG_CONTAINER = null;

  public static class CommonConfig {
    public static ForgeConfigSpec.BooleanValue restricted_to_nametags;
    public static ForgeConfigSpec.BooleanValue requires_ink;

    CommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("general");
      restricted_to_nametags = builder
                .comment("Is renaming restricted to nametags")
                .define("restricted_to_nametags", true);
      requires_ink = builder
              .comment("When true, will consume 1 ink sac per rename")
              .define("requires_ink", false);
    }
  }
}
