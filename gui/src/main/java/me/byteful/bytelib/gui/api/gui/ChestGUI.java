package me.byteful.bytelib.gui.api.gui;

import me.byteful.bytelib.core.util.Formatting;
import me.byteful.bytelib.gui.GUIModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;

public abstract class ChestGUI {
  public abstract Character[][] getLayout();

  @NotNull
  public abstract Map<Character, ChestGUIButton> getButtons();

  @NotNull
  public abstract Map<String, BiConsumer<String, InventoryClickEvent>> getActions();

  @NotNull
  public abstract String getTitle();

  public final void draw(@NotNull Inventory inventory, @NotNull Player player) {
    final Character[][] layout = getLayout();
    for (int y = 0; y < layout.length; y++) {
      final Character[] row = layout[y];
      for (int x = 0; x < row.length; x++) {
        final Character slot = row[x];
        final ChestGUIButton button = getButtons().get(slot);
        if (button != null) {
          inventory.setItem(x + (y * 9), button.getItemStack(player));
        }
      }
    }

    onDraw(inventory, player);
  }

  protected abstract void onDraw(@NotNull Inventory inventory, @NotNull Player player);

  public void onClose(@NotNull Inventory inventory, @NotNull Player player) {
  }

  @NotNull
  public Inventory build(@NotNull Player player) {
    final Inventory inv = Bukkit.createInventory(null, getLayout().length * 9, Formatting.color(getTitle()));
    draw(inv, player);

    return inv;
  }

  public void open(@NotNull Player player) {
    final Inventory inv = build(player);
    player.openInventory(inv);
    GUIModule.getInstance().getTrackedInventories().put(inv, this);
  }

  public void cleanup(@NotNull Inventory inventory, boolean close) {
    if (close) {
      inventory.getViewers().forEach(HumanEntity::closeInventory);
    }
    inventory.clear();
  }
}
