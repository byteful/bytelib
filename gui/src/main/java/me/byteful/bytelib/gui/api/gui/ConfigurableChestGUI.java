package me.byteful.bytelib.gui.api.gui;

import me.byteful.bytelib.core.util.ItemParser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Configuration example:
 * <p>
 * gui:
 * buttons:
 * 'a':
 * name: "test"
 * lore:
 * - "line1"
 * - "line2"
 * material: GOLD_INGOT
 * layout: # number of lines determines number of rows
 * - "  a   a  " # spaces are reserved for empty slots
 * - "         "
 */
public abstract class ConfigurableChestGUI implements ChestGUI {
  private final Character[][] layout;
  private final Map<Character, ItemStack> buttons = new HashMap<>();
  private final Map<String, Consumer<InventoryClickEvent>> actions = new HashMap<>();

  public ConfigurableChestGUI(ConfigurationSection config) {
    final ConfigurationSection buttonsConfig = config.getConfigurationSection("buttons");
    if (buttonsConfig == null) {
      throw new RuntimeException("Failed to parse GUI config for buttons. None found!");
    }
    buttonsConfig.getValues(false).forEach((id, item) -> {
      final ItemStack itemStack = ItemParser.fromConfig((ConfigurationSection) item);
      buttons.put(id.charAt(0), itemStack);
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

  @Override
  public Character[][] getLayout() {
    return layout;
  }

  @Override
  public Map<Character, ItemStack> getButtons() {
    return buttons;
  }

  @Override
  public Map<String, Consumer<InventoryClickEvent>> getActions() {
    return actions;
  }

  protected void registerAction(String id, Consumer<InventoryClickEvent> callback) {
    actions.put(id, callback);
  }

  @Override
  public void open(Player player) {

  }
}
