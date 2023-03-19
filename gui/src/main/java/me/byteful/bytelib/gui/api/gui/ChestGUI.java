package me.byteful.bytelib.gui.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public interface ChestGUI {
  Character[][] getLayout();

  Map<Character, ItemStack> getButtons();

  Map<String, Consumer<InventoryClickEvent>> getActions();

  void draw(Player player);

  void open(Player player);
}
