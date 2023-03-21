package me.byteful.bytelib.gui.api.gui;

import me.byteful.bytelib.core.util.ItemParser;
import me.byteful.bytelib.core.util.TriFunction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ConfigurableChestGUI extends ChestGUI {
  private final Character[][] layout;
  private final Map<Character, ChestGUIButton> buttons = new HashMap<>();
  private final Map<String, BiConsumer<String, InventoryClickEvent>> actions = new HashMap<>();
  private final String title;

  public ConfigurableChestGUI(ConfigurationSection config, TriFunction<Player, ItemStack, String, ItemStack> formatItemStack) {
    this.title = config.getString("title");
    final ConfigurationSection buttonsConfig = config.getConfigurationSection("buttons");
    if (buttonsConfig == null) {
      throw new RuntimeException("Failed to parse GUI config for buttons. None found!");
    }
    buttonsConfig.getValues(false).forEach((id, item) -> {
      final ConfigurableChestGUIButton button = new ConfigurableChestGUIButton((ConfigurationSection) item, formatItemStack);
      buttons.put(id.charAt(0), button);
    });
    final List<String> layoutConfig = config.getStringList("layout");
    if (layoutConfig.isEmpty()) {
      throw new RuntimeException("Failed to parse GUI config for layout. Not enough rows!");
    }
    if (layoutConfig.size() > 6) {
      throw new RuntimeException("Too many rows! 6 is max.");
    }
    layout = new Character[layoutConfig.size()][9];
    for (int y = 0; y < layoutConfig.size(); y++) {
      final char[] row = layoutConfig.get(y).toCharArray();
      for (int x = 0; x < row.length; x++) {
        layout[y][x] = row[x];
      }
    }
  }

  @NotNull
  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public Character[][] getLayout() {
    return layout;
  }

  @Override
  @NotNull
  public Map<Character, ChestGUIButton> getButtons() {
    return buttons;
  }

  @Override
  public @NotNull Map<String, BiConsumer<String, InventoryClickEvent>> getActions() {
    return actions;
  }

  protected void registerAction(@NotNull String id, @NotNull BiConsumer<String, InventoryClickEvent> callback) {
    actions.put(id, callback);
  }

  protected void registerAction(@NotNull String id, @NotNull Consumer<InventoryClickEvent> callback) {
    actions.put(id, (meta, e) -> callback.accept(e));
  }

  private final static class ConfigurableChestGUIButton implements ChestGUIButton {
    private final ItemStack display;
    private final String actionId, actionMeta;
    private final TriFunction<Player, ItemStack, String, ItemStack> formatItemStack;

    public ConfigurableChestGUIButton(ConfigurationSection config, TriFunction<Player, ItemStack, String, ItemStack> formatItemStack) {
      this.formatItemStack = formatItemStack;
      this.display = ItemParser.fromConfig(config);
      this.actionId = config.getString("action");
      this.actionMeta = config.getString("action_meta");
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull Player viewer) {
      return formatItemStack.apply(viewer, display, actionMeta);
    }

    @Override
    public String getActionId() {
      return actionId;
    }

    @Override
    public @Nullable String getActionMetadata() {
      return actionMeta;
    }
  }
}
