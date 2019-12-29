package com.tfar.nametagswithoutanvil;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NametagsWithoutAnvil.MODID)
public class NametagsWithoutAnvil implements INamedContainerProvider {
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
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    // Register the setup method for modloading
    bus.addListener(this::setup);
    // Register the doClientStuff method for modloading
    bus.addListener(this::doClientStuff);
    EVENT_BUS.addListener(this::rightClick);
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
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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

  private void rightClick(PlayerInteractEvent.RightClickItem e) {
    PlayerEntity player = e.getPlayer();
    if (e.getItemStack().getItem().isIn(quill) && (player.getHeldItemOffhand().getItem() == Items.NAME_TAG || !CommonConfig.restricted_to_nametags.get()) && !player.world.isRemote) {
      NetworkHooks.openGui((ServerPlayerEntity) player, this);
    }
  }

  @Nonnull
  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent("rename");
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory inv, PlayerEntity player) {
    return new NameTagContainer(i, inv);
  }
}
