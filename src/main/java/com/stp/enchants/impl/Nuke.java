package com.stp.enchants.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;
import com.stp.utils.MessageUtils;
import com.stp.utils.MineRegion;
import com.stp.utils.PlaceholderUtil;
import com.stp.utils.WorldGuardUtils;

public class Nuke implements CustomEnchant {
    private final int level;
    private final Random random = new Random();
    private final String displayName;
    private final int maxLevel;
    private final boolean enabled;

    public static ThreadLocal<Boolean> isNuking = ThreadLocal.withInitial(() -> false);

    public Nuke(int level) {
        this.level = level;
        this.displayName = PrisonEnchantCustom.getInstance().getConfig().getString("enchants.nuke.display", "&cNuke");
        this.maxLevel = PrisonEnchantCustom.getInstance().getConfig().getInt("enchants.nuke.max-level", 1);
        this.enabled = PrisonEnchantCustom.getInstance().getConfig().getBoolean("enchants.nuke.enabled", true);
    }

    @Override
    public String getId() {
        return "nuke";
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
        if (!enabled || item == null) return false;

        List<String> allowedTypes = PrisonEnchantCustom.getInstance().getConfig()
            .getStringList("enchants." + getId() + ".enchants-item-avaible");
        boolean strict = PrisonEnchantCustom.getInstance().getConfig()
            .getBoolean("enchants." + getId() + ".enchant-strict", false);

        String typeName = item.getType().name();

        boolean typeAllowed = allowedTypes.stream().anyMatch(typeName::endsWith);
        if (!typeAllowed) return false;

        if (strict) {

            String requiredName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("pickaxe.display-name", "");
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
            String displayName = item.getItemMeta().getDisplayName();

            return displayName.equals(requiredName.replace("&", "§"));
        }

        return true;
    }

    public void handleBlockBreak(BlockBreakEvent event, Player player, int level) {
        if (!enabled || level <= 0) return;

        double chance = PrisonEnchantCustom.getInstance().getConfig().getDouble("enchants.nuke.chance", 0.1);

        if (random.nextDouble() < (chance / 100.0)) {
            MineRegion region = WorldGuardUtils.getMineRegion(event.getBlock());
            if (region != null) {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("enchant", displayName);
                player.sendMessage(
                    PlaceholderUtil.applyPlaceholders(
                        player,
                        MessageUtils.getMessage("nuke.activated"),
                        placeholders
                    )
                );
                nukeMine(player, event.getBlock().getWorld(), region);
            } else {
                player.sendMessage(
                    PlaceholderUtil.applyPlaceholders(
                        player,
                        MessageUtils.getMessage("nuke.invalid-mine"),
                        new HashMap<>()
                    )
                );
            }
        }
    }

    private void nukeMine(Player player, World world, MineRegion region) {
        isNuking.set(true);

        List<Material> allowedBlocks = PrisonEnchantCustom.getInstance().getAllowedBlocks();
        ItemStack tool = player.getInventory().getItemInHand();

        for (int x = region.getMinX(); x <= region.getMaxX(); x++) {
            for (int y = region.getMinY(); y <= region.getMaxY(); y++) {
                for (int z = region.getMinZ(); z <= region.getMaxZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (!allowedBlocks.contains(block.getType()) || block.getType() == Material.AIR) continue;

                    BlockBreakEvent fakeEvent = new BlockBreakEvent(block, player);
                    Bukkit.getPluginManager().callEvent(fakeEvent);

                    if (!fakeEvent.isCancelled()) {
                        block.breakNaturally(tool);
                    }
                }
            }
        }

        isNuking.set(false);
    }
}