package us.herbalcraft.LockPlug;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Events {
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (Objects.equals(p.getInventory().getItemInMainHand(), "redstone")) {
            Location loc = Player.getEyeLocation();
            Block b = Player.getTargetBlock(null, 5);
            Vector v = loc.getDirection().normalize();

            for(int i = 1 ; i <= 5 ; i++) {
                loc.add(v);
                if(loc.getBlock().getType() != Material.AIR)
                    break;
            }
            Block targetedBlock = loc.getBlock();
            if(targetedBlock.getState() instanceof InventoryHolder) {

            }
        }

    }
}
