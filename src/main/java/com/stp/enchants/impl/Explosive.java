package com.stp.enchants.impl;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;

public class Explosive implements CustomEnchant {
    private final int level;
    private final Random random = new Random();
    private boolean lastActivation = false;
    private final String displayName;
    private final String pickaxeName;
    private final int maxLevel;
    private final boolean enabled;


    public static ThreadLocal<Boolean> isExploding = ThreadLocal.withInitial(() -> false);

    public Explosive(int level) {
        this.level = level;
        this.displayName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("enchants.explosive.display", "&7Explosive");
        this.maxLevel = PrisonEnchantCustom.getInstance().getConfig()
                .getInt("enchants.explosive.max-level", 50);
        this.enabled = PrisonEnchantCustom.getInstance().getConfig()
                .getBoolean("enchants.explosive.enabled", true);
        this.pickaxeName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("pickaxe.display-name", "&f&lPICO &7&l| &a&lINICIAL");
    }

    @Override
    public String getId() {
        return "explosive";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void onEnable(Player player, int level) {}

    @Override
    public void onDisable(Player player) {}

    @Override
    public void applyEffect(Player player, int level) {}

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return enabled && item != null && item.getType().toString().contains(pickaxeName);
    }

    public void handleBlockBreak(BlockBreakEvent event, Player player, int level) {
        if (!enabled) return;

        if (!lastActivation) {
            boolean activate = random.nextInt(3) == 0;
            if (activate) {
                destroyBlocks(player, event.getBlock(), level);
            }
            lastActivation = activate;
        } else {
            lastActivation = false;
        }
    }

    private void destroyBlocks(Player player, Block origin, int level) {
        List<Material> allowedBlocks = PrisonEnchantCustom.getInstance().getAllowedBlocks();

        int radius = Math.min((level - 1) / 10 + 1, 3);
        World world = origin.getWorld();
        Location originLoc = origin.getLocation();

        isExploding.set(true);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    Block block = world.getBlockAt(originLoc.clone().add(x, y, z));
                    if (!allowedBlocks.contains(block.getType()) || block.getType() == Material.AIR) continue;

                    BlockBreakEvent fakeEvent = new BlockBreakEvent(block, player);
                    Bukkit.getPluginManager().callEvent(fakeEvent);
                    if (!fakeEvent.isCancelled()) {
                        block.breakNaturally(player.getInventory().getItemInHand());
                    }
                }
            }
        }

        isExploding.set(false); 
    }
}