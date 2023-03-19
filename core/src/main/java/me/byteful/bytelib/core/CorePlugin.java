package me.byteful.bytelib.core;

import me.byteful.bytelib.core.api.module.Module;
import me.byteful.bytelib.core.api.module.ModuleManager;
import me.byteful.bytelib.core.api.module.impl.StaticModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CorePlugin extends JavaPlugin {
  protected ModuleManager moduleManager;

  public void load() {
  }

  public abstract void enable(); // You've always gotta do something on plugin enable...

  public void disable() {
  }

  protected abstract Module[] getModules();

  protected ModuleManager createModuleManager() {
    return new StaticModuleManager(getModules());
  }

  @Override
  public final void onLoad() {
    CoreProvider.init(this);
    moduleManager = createModuleManager();
    load();
  }

  @Override
  public final void onEnable() {
    moduleManager.loadAll();
    enable();
  }

  @Override
  public final void onDisable() {
    moduleManager.unloadAll();
    disable();
  }
}
