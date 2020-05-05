package us.herbalcraft.LockPlug;

import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class LockPlug extends JavaPlugin {
    FileConfiguration config = getConfig();
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin has been Enabled\n\n");
        getServer().getPluginManager().registerEvents(new Events(), this);
    }
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Plugin has been Disabled\n\n");
    }
    public class Events implements Listener {
        public Economy econ = null;

        private boolean setupEconomy() {
            if (getServer().getPluginManager().getPlugin("Vault") == null) {
                return false;
            }
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            }
            econ = rsp.getProvider();
            assert econ != null;
            return econ != null;
        }

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            Action action = event.getAction();
            Player p = event.getPlayer();
            Block block = event.getClickedBlock();

            if(action.equals(Action.LEFT_CLICK_BLOCK)) {
                assert block != null;
                p.sendMessage("LockPlug Debug [INFO]: Block Hit Detected.");
                System.out.println("LockPlug Debug [INFO]: Block Hit Detected.");
                if (p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE)) {
                    System.out.println("LockPlug Debug [INFO]: Redstone Hit Detected.");
                    p.sendMessage("LockPlug Debug [INFO]: Redstone Hit Detected.");
                    if (block.getType().equals(Material.CHEST)) {
                        System.out.println("LockPlug Debug [INFO]: Chest Detected.");
                        p.sendMessage("LockPlug Debug [INFO]: Chest Detected.");
                        Chest cblock = (Chest) block.getState();
                        p.sendMessage("LockPlug Debug [INFO]: cblock variable successfully initialized.");
                        p.sendMessage(String.valueOf(cblock));
                        if (!(cblock.isLocked())) {
                            if (econ.getBalance(p) >= (Integer) config.get("Charge")) {
                                System.out.println("LockPlug Debug [INFO]: Chest is NOT Locked and Player has balance of " + config.get("Charge") + " or more.");
                                p.sendMessage("LockPlug Debug [INFO]: Chest is NOT Locked and Player has balance of " + config.get("Charge") + " or more.");
                                cblock.setLock(String.valueOf(p));
                                econ.withdrawPlayer(p, (Integer) config.get("Charge"));
                                TextComponent success = new TextComponent("LockPlug: The chest has been successfully locked and your balance has been deducted by ");
                                success.addExtra((String) config.get("Charge"));
                                success.addExtra(" coins.");
                                success.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                                p.sendMessage(String.valueOf(success));
                                System.out.println("LockPlug Debug [INFO]: Chest has been LOCKED and Balance of" + p + "has been deducted by " + config.get("Charge") + ".");
                                p.sendMessage("LockPlug Debug [INFO]: Chest has been LOCKED and Balance of" + p + "has been deducted by " + config.get("Charge") + ".");
                            } else if (cblock.isLocked()) {
                                TextComponent alreadyLocked = new TextComponent("LockPlug: The chest is already locked by ");
                                alreadyLocked.addExtra(cblock.getLock());
                                alreadyLocked.addExtra("! Your balance was not deducted.");
                                alreadyLocked.setColor(net.md_5.bungee.api.ChatColor.RED);
                                p.sendMessage(String.valueOf(alreadyLocked));
                                System.out.println("LockPlug Debug [INFO]: Chest was locked already by " + cblock.getLock() + ". " + p + "'s balance was not deducted.");
                                p.sendMessage("LockPlug Debug [INFO]: Chest was locked already by " + cblock.getLock() + ". " + p + "'s balance was not deducted.");
                            } else if (!(econ.getBalance(p) >= (Integer) config.get("Charge"))) {
                                TextComponent notEnoughMoney = new TextComponent("LockPlug: Your balance is too low! You must have a minimum of ");
                                notEnoughMoney.addExtra(String.valueOf(config.get("Charge")));
                                notEnoughMoney.addExtra(" coins to lock a chest!");
                                notEnoughMoney.setColor(net.md_5.bungee.api.ChatColor.RED);
                                p.sendMessage(String.valueOf(notEnoughMoney));
                                System.out.println("LockPlug Debug [INFO]: " + p + "'s balance was too low to lock the chest! Their balance was not deducted.");
                                p.sendMessage("LockPlug Debug [INFO]: " + p + "'s balance was too low to lock the chest! Their balance was not deducted.");
                            } else {
                                TextComponent otherError = new TextComponent("LockPlug: An unknown error occurred! This event has been logged and will be investigated!");
                                otherError.setColor(net.md_5.bungee.api.ChatColor.DARK_RED);
                                p.sendMessage(String.valueOf(otherError));
                                System.out.println("LockPlug Debug [WARN]: An UNKNOWN ERROR occurred while p " + p + " was using LockPlug! Please check for a STACKTRACE!");
                                p.sendMessage("LockPlug Debug [WARN]: An UNKNOWN ERROR occurred while p " + p + " was using LockPlug! Please check for a STACKTRACE!");
                            }
                        }
                    }
                }
            }
        }
    }
}

