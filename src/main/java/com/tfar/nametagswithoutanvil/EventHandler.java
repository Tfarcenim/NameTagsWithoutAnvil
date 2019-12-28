package com.tfar.nametagswithoutanvil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
@Mod.EventBusSubscriber(modid = NametagsWithoutAnvil.MODID)
public class EventHandler {

  @SubscribeEvent
  public static void rightClick(PlayerInteractEvent.RightClickItem e){
   PlayerEntity player = e.getPlayer();
   if (e.getItemStack().getItem().isIn(NametagsWithoutAnvil.quill) && (player.getHeldItemOffhand().getItem() == Items.NAME_TAG || !NametagsWithoutAnvil.CommonConfig.restricted_to_nametags.get()) && !player.world.isRemote){
     NetworkHooks.openGui((ServerPlayerEntity) player,new ContainerProvider());
   }
  }
}
