package us.herbalcraft.LockPlug;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public class Events extends JavaPlugin implements Listener, Cancellable {
    FileConfiguration config = getConfig();
    public static Economy econ = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @EventHandler
    public Listener onBlockBreak(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            System.out.println("LockPlug Debug: Block Break Detected.");
            Block block = event.getClickedBlock();
            event.setCancelled(true);
            if (p.getInventory().getItemInMainHand().getType() == Material.REDSTONE && block instanceof InventoryHolder) {
                System.out.println("LockPlug Debug: Chest Detected.");
                Chest cblock = (Chest) block;
                if (!(cblock.isLocked()) && econ.getBalance(p) >= Integer.valueOf(String.valueOf(config.get("Charge")))) {
                    System.out.println("LockPlug Debug: Chest is NOT Locked and Player has balance of " + config.get("Charge") + " or more.");
                    cblock.setLock(String.valueOf(p));
                    econ.withdrawPlayer(p, 200);
                    TextComponent success = new TextComponent("The chest has been successfully locked and your balance has been deducted by ");
                    success.addExtra(String.valueOf(config.get("Charge")));
                    success.addExtra(" coins.");
                    success.setColor(ChatColor.GREEN);
                    p.sendMessage(String.valueOf(success));
                    System.out.println("LockPlug Debug [INFO]: Chest has been LOCKED and Balance of" + p + "has been deducted by " + config.get("Charge") + ".");
                } else if (cblock.isLocked()) {
                    TextComponent alreadyLocked = new TextComponent("The chest is already locked by ");
                    alreadyLocked.addExtra(cblock.getLock());
                    alreadyLocked.addExtra("! Your balance was not deducted.");
                    alreadyLocked.setColor(ChatColor.RED);
                    p.sendMessage(String.valueOf(alreadyLocked));
                    System.out.println("LockPlug Debug [INFO]: Chest was locked already by " + cblock.getLock() + ". " + p + "'s balance was not deducted.");
                } else if (!(econ.getBalance(p) >= Integer.valueOf(String.valueOf(config.get("Charge"))))) {
                    TextComponent notEnoughMoney = new TextComponent("Your balance is too low! You must have a minimum of ");
                    notEnoughMoney.addExtra(String.valueOf(config.get("Charge")));
                    notEnoughMoney.addExtra(" coins to lock a chest!");
                    notEnoughMoney.setColor(ChatColor.RED);
                    p.sendMessage(String.valueOf(notEnoughMoney));
                    System.out.println("LockPlug Debug [INFO]: " + p + "'s balance was too low to lock the chest! Their balance was not deducted.");
                } else {
                    TextComponent otherError = new TextComponent("An unknown error occurred! This event has been logged and will be investigated!");
                    otherError.setColor(ChatColor.DARK_RED);
                    p.sendMessage(String.valueOf(otherError));
                    System.out.println("LockPlug Debug [WARN]: An UNKNOWN ERROR occurred while player " + p + " was using LockPlug! Please check for a STACKTRACE!");
                }
            }
        }
        return null;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

}
