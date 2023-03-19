package me.byteful.bytelib.gui;

import me.byteful.bytelib.core.api.module.Module;

public final class GUIModule implements Module {
  private static GUIModule instance;

  private GUIModule() {
  }

  public static GUIModule getInstance() {
    return instance != null ? instance : (instance = new GUIModule());
  }

  @Override
  public void load() {

  }

  @Override
  public void unload() {

  }
}
