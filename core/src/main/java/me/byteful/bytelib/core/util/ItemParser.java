package me.byteful.bytelib.core.util;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static me.byteful.bytelib.core.util.Formatting.color;

public final class ItemParser {
  public static ItemStack fromConfig(ConfigurationSection config) {
    final String name = config.getString("name");
    final List<String> lore = config.getStringList("lore");
    final String material = config.getString("material");

    if (material == null) {
      throw new RuntimeException("Material for config item (" + config.getName() + ") cannot be null!");
    }

    final ItemStack item = XMaterial.matchXMaterial(material).orElseThrow(RuntimeException::new).parseItem();
    assert item != null : "Failed to parse XMaterial item: " + material;
    if (item.getType() == Material.AIR) {
      return item;
    }
    final ItemMeta meta = item.getItemMeta();
    assert meta != null : "Meta should not be null!";
    if (name != null) {
      meta.setDisplayName(color(name));
    }
    if (!lore.isEmpty()) {
      meta.setLore(color(lore));
    }

    item.setItemMeta(meta);
    return item;
  }
}
