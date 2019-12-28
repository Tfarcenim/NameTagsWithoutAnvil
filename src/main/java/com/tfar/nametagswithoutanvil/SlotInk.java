package com.tfar.nametagswithoutanvil;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotInk extends SlotItemHandler {
  public SlotInk(IItemHandler p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
    super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack p_75214_1_) {
    return p_75214_1_.getItem() == Items.INK_SAC;
  }
}
