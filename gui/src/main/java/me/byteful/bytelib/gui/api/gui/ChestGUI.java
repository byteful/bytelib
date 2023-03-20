package me.byteful.bytelib.gui.api.gui;

import me.byteful.bytelib.core.CoreProvider;
import me.byteful.bytelib.core.util.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ChestGUI implements Listener {
  private static final Set<Inventory> TRACKED_INVENTORIES = new HashSet<>();

  public ChestGUI() {
    Bukkit.getPluginManager().registerEvents(this, CoreProvider.get());
  }

  public abstract Character[][] getLayout();

  @NotNull
  public abstract Map<Character, ChestGUIButton> getButtons();

  @NotNull
  public abstract Map<String, Consumer<InventoryClickEvent>> getActions();

  @NotNull
  public abstract String getTitle();

  public abstract void draw(@NotNull Inventory inventory, @NotNull Player player);

  @NotNull
  public Inventory build(@NotNull Player player) {
    final Inventory inv = Bukkit.createInventory(null, getLayout().length, Formatting.color(getTitle()));
    draw(inv, player);
    TRACKED_INVENTORIES.add(inv);

    return inv;
  }

  public void open(@NotNull Player player) {
    player.openInventory(build(player));
  }

  private void cleanup(@NotNull Inventory inventory) {
    if (TRACKED_INVENTORIES.remove(inventory)) {
      inventory.getViewers().forEach(HumanEntity::closeInventory);
      inventory.clear();
    }
  }

  @EventHandler
  public void on(InventoryCloseEvent event) {
    final Inventory inventory = event.getInventory();
    if (inventory.getViewers().isEmpty()) {
      cleanup(inventory);
    }
  }

  @EventHandler
  public void on(PluginDisableEvent event) {
    if (event.getPlugin() == CoreProvider.get()) {
      TRACKED_INVENTORIES.forEach(this::cleanup);
    }
  }

  @EventHandler
  public void on(InventoryDragEvent event) {
    if (TRACKED_INVENTORIES.contains(event.getInventory())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void on(InventoryClickEvent event) {
    if (!TRACKED_INVENTORIES.contains(event.getView().getTopInventory())) {
      return;
    }
    if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR && !TRACKED_INVENTORIES.contains(event.getClickedInventory())) {
      event.setCancelled(true);
      return;
    }
    if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && !TRACKED_INVENTORIES.contains(event.getClickedInventory())) {
      event.setCancelled(true);
    }
    if (event.getInventory().equals(event.getClickedInventory())) {
      event.setCancelled(true);
      int slot = event.getSlot();
      int x = slot % 9;
      int y = slot / 9;
      final Character buttonId = getLayout()[y][x];
      final ChestGUIButton button = getButtons().get(buttonId);
      if (button != null) {
        final String actionId = button.getActionId();
        final Consumer<InventoryClickEvent> action = getActions().get(actionId);
        if (action != null) {
          action.accept(event);
        }
      }
    }
  }
}
