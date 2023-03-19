package me.byteful.bytelib.core;

public final class CoreProvider {
  private static CorePlugin plugin;

  static void init(CorePlugin plugin) {
    CoreProvider.plugin = plugin;
  }

  public static CorePlugin get() {
    if (plugin == null) {
      throw new RuntimeException("Please wait until bytelib loads!");
    }

    return plugin;
  }
}
