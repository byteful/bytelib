package me.byteful.bytelib.gui;

import me.byteful.bytelib.core.CoreProvider;
import me.byteful.bytelib.core.api.module.Module;
import me.byteful.bytelib.core.util.Task;
import me.byteful.bytelib.gui.api.gui.ChestGUI;
import me.byteful.bytelib.gui.api.gui.ChestGUIButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class GUIModule implements Module, Listener {
  private static GUIModule instance;

  private final Map<Inventory, ChestGUI> trackedInventories = new HashMap<>();

  private GUIModule() {
  }

  public static GUIModule getInstance() {
    return instance != null ? instance : (instance = new GUIModule());
  }

  @Override
  public void load() {
    Bukkit.getPluginManager().registerEvents(this, CoreProvider.get());
  }

  @Override
  public void unload() {

  }

  public Map<Inventory, ChestGUI> getTrackedInventories() {
    return trackedInventories;
  }

  @EventHandler
  public void on(InventoryCloseEvent event) {
    if (!(event.getPlayer() instanceof Player)) {
      return;
    }
    final Inventory inventory = event.getInventory();
    final ChestGUI gui = trackedInventories.get(inventory);
    if (gui == null) {
      return;
    }
    if (inventory.getViewers().size() - 1 <= 0) {
      gui.cleanup(inventory, false);
      gui.onClose(inventory, (Player) event.getPlayer());
    }
  }

  @EventHandler
  public void on(PluginDisableEvent event) {
    if (event.getPlugin() == CoreProvider.get()) {
      trackedInventories.forEach((inv, gui) -> gui.cleanup(inv, true));
    }
  }

  @EventHandler
  public void on(InventoryDragEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) {
      return;
    }
    if (trackedInventories.containsKey(event.getInventory())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void on(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) {
      return;
    }
    final ChestGUI gui = trackedInventories.get(event.getInventory());
    if (gui == null) {
      return;
    }
    if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR && !trackedInventories.containsKey(event.getClickedInventory())) {
      event.setCancelled(true);
      return;
    }
    if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && !trackedInventories.containsKey(event.getClickedInventory())) {
      event.setCancelled(true);
    }
    if (event.getInventory().equals(event.getClickedInventory())) {
      event.setCancelled(true);
      int slot = event.getSlot();
      int x = slot % 9;
      int y = slot / 9;
      final Character buttonId = gui.getLayout()[y][x];
      final ChestGUIButton button = gui.getButtons().get(buttonId);
      if (button != null) {
        final String actionId = button.getActionId();
        final BiConsumer<String, InventoryClickEvent> action = gui.getActions().get(actionId);
        if (action != null) {
          Task.sync(() -> action.accept(button.getActionMetadata(), event));
        }
      }
    }
  }
}
