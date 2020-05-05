package us.herbalcraft.LockPlug;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LockPlug extends JavaPlugin {
    FileConfiguration config = getConfig();
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new Events(), this);
    }
}
