package com.tfar.nametagswithoutanvil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketInkRequired {

  public SPacketInkRequired() {}

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //if (ctx.get() == null || ctx.get().getSender() == null)return;
      PlayerEntity player = DistExecutor.callWhenOn(Dist.CLIENT,() -> () -> Minecraft.getInstance().player);
      if (player == null)return;
      if (player.openContainer instanceof NameTagContainer){
        ((NameTagContainer) player.openContainer).timesincewarning = 200;
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
