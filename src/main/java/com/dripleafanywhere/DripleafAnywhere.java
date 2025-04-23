package com.dripleafanywhere;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class DripleafAnywhere extends JavaPlugin implements Listener {

    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        // Register the event listener to listen for player interactions
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("DripleafAnywhere plugin enabled successfully!");
        // Config for paranoid mode
        config.addDefault("paranoidMode", false);
        if (config.getBoolean("paranoidMode")) {
          getLogger().info("Paranoid Mode has been enabled, have fun!");
        }
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("DripleafAnywhere plugin disabled.");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if the interaction is a right-click with the main hand
        if (event.getAction().toString().contains("RIGHT_CLICK") && event.getHand() == EquipmentSlot.HAND) {
            // If the player right-clicked on a block
            if (event.hasBlock()) {
                Material blockType = event.getClickedBlock().getType();
                // Log the block the player right-clicked on
                if (config.getBoolean("paranoidMode")) getLogger().info("Player right-clicked on a block: " + blockType);
                // Check if item in hand is dripleaf; otherwise any block you place is gonna be a dripleaf
                if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BIG_DRIPLEAF)) {
                    Block clickedBlock = event.getClickedBlock();
                    BlockData data = Material.BIG_DRIPLEAF.createBlockData();

                    // Try to place the Big Dripleaf in the block above the clicked block (without removing the clicked block)
                    Block aboveBlock = clickedBlock.getRelative(0, 1, 0); // Block directly above the clicked block

                    // Only place the Big Dripleaf if the block above is air or water
                    if (aboveBlock.getType() == Material.AIR || aboveBlock.getType() == Material.WATER) {
                        aboveBlock.setType(Material.BIG_DRIPLEAF);
                        aboveBlock.setBlockData(data);

                        if (config.getBoolean("paranoidMode")) getLogger().info("Placed Big Dripleaf above: " + aboveBlock.getLocation());
                    }
                }
            }
        }
    }
}
