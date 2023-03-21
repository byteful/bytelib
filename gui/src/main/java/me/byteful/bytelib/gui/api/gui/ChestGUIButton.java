package me.byteful.bytelib.gui.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ChestGUIButton {
  @NotNull ItemStack getItemStack(@NotNull Player viewer);

  @Nullable String getActionId();

  @Nullable String getActionMetadata();
}
