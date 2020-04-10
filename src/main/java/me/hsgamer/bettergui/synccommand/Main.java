package me.hsgamer.bettergui.synccommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import me.hsgamer.bettergui.object.addon.Addon;
import org.bukkit.Bukkit;

public final class Main extends Addon {

  private Method syncCommandsMethod;

  @Override
  public boolean onLoad() {
    try {
      Class<?> craftServer;
      String revision = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
      craftServer = Class.forName("org.bukkit.craftbukkit." + revision + ".CraftServer");

      syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
      if (syncCommandsMethod != null) {
        syncCommandsMethod.setAccessible(true);
        return true;
      } else {
        return false;
      }
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      getPlugin().getLogger().log(Level.WARNING, "Error when loading the addon", e);
      return false;
    }
  }

  @Override
  public void onPostEnable() {
    try {
      syncCommandsMethod.invoke(getPlugin().getServer());
    } catch (IllegalAccessException | InvocationTargetException e) {
      getPlugin().getLogger().log(Level.WARNING, "Error when syncing commands", e);
    }
  }
}
