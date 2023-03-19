package me.byteful.bytelib.core.util;

import me.byteful.bytelib.core.CoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class Task {
  public static BukkitTask sync(Runnable run) {
    return Bukkit.getScheduler().runTask(CoreProvider.get(), run);
  }

  public static BukkitTask async(Runnable run) {
    return Bukkit.getScheduler().runTaskAsynchronously(CoreProvider.get(), run);
  }

  public static BukkitTask syncRepeating(Runnable run, long delay, long period) {
    return Bukkit.getScheduler().runTaskTimer(CoreProvider.get(), run, delay, period);
  }

  public static BukkitTask syncRepeating(Runnable run, long period) {
    return syncRepeating(run, 0, period);
  }

  public static BukkitTask asyncRepeating(Runnable run, long delay, long period) {
    return Bukkit.getScheduler().runTaskTimerAsynchronously(CoreProvider.get(), run, delay, period);
  }

  public static BukkitTask asyncRepeating(Runnable run, long period) {
    return asyncRepeating(run, 0, period);
  }

  public static BukkitTask syncLater(Runnable run, long delay) {
    return Bukkit.getScheduler().runTaskLater(CoreProvider.get(), run, delay);
  }

  public static BukkitTask asyncLater(Runnable run, long delay) {
    return Bukkit.getScheduler().runTaskLaterAsynchronously(CoreProvider.get(), run, delay);
  }
}
