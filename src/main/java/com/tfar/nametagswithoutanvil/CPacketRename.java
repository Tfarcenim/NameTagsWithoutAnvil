package com.tfar.nametagswithoutanvil;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketRename {

  private String name;
  private int length;

  public CPacketRename() {}

  public CPacketRename(String newName) {
    this.name = newName;
  }

 public CPacketRename(PacketBuffer buf) {
    length = buf.readInt();
   name = buf.readString(length);
  }

  public void encode(PacketBuffer buf) {
    buf.writeInt(name.length());
    buf.writeString(name);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      if (ctx.get() == null || ctx.get().getSender() == null)return;
      ServerPlayerEntity player = ctx.get().getSender();
      Container anvil = ctx.get().getSender().openContainer;
      if (anvil instanceof NameTagContainer){
        if (NametagsWithoutAnvil.CommonConfig.requires_ink.get()){
          if (!((NameTagContainer) anvil).ink.getStackInSlot(0).isEmpty()) {
            ((NameTagContainer) anvil).ink.getStackInSlot(0).shrink(1);
            player.getHeldItemOffhand().setDisplayName(new StringTextComponent(name));
          } else PacketHandler.INSTANCE.sendTo( new SPacketInkRequired(), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);

        } else player.getHeldItemOffhand().setDisplayName(new StringTextComponent(name));

      }
    });
    ctx.get().setPacketHandled(true);
  }
}
