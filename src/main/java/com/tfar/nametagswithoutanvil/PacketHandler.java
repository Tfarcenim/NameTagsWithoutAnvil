package com.tfar.nametagswithoutanvil;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public class PacketHandler {

  public static SimpleChannel INSTANCE;

  public static void registerMessages(String channelName) {
    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(NametagsWithoutAnvil.MODID, channelName), () -> "1.0", s -> true, s -> true);
    INSTANCE.registerMessage(0, CPacketRename.class,
            CPacketRename::encode,
            CPacketRename::new,
            CPacketRename::handle);

    INSTANCE.registerMessage(1, SPacketInkRequired.class,
            (msg, buffer) -> {},
            (t) -> new SPacketInkRequired(),
            SPacketInkRequired::handle);
  }
}