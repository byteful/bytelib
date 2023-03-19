package me.byteful.bytelib.core.api.module.impl;

import me.byteful.bytelib.core.api.module.Module;
import me.byteful.bytelib.core.api.module.ModuleManager;

public class StaticModuleManager implements ModuleManager {
  private final Module[] modules;

  public StaticModuleManager(Module[] modules) {
    this.modules = modules;
  }

  @Override
  public void loadAll() {
    for (Module module : modules) {
      module.load();
    }
  }

  @Override
  public void unloadAll() {
    for (Module module : modules) {
      module.unload();
    }
  }
}
