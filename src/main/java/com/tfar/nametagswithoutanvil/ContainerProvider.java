package com.tfar.nametagswithoutanvil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerProvider implements INamedContainerProvider {

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
