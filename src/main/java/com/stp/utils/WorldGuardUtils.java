package com.stp.utils;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardUtils {

    public static MineRegion getMineRegion(Block block) {
        WorldGuardPlugin wg = getWorldGuard();
        if (wg == null) return null;

        RegionManager manager = wg.getRegionManager(block.getWorld());
        if (manager == null) return null;

        ApplicableRegionSet regions = manager.getApplicableRegions(block.getLocation());
        for (ProtectedRegion region : regions) {
            if (region.getId().startsWith("mina_")) {
                return new MineRegion(
                        region.getMinimumPoint().getBlockX(), region.getMaximumPoint().getBlockX(),
                        region.getMinimumPoint().getBlockY(), region.getMaximumPoint().getBlockY(),
                        region.getMinimumPoint().getBlockZ(), region.getMaximumPoint().getBlockZ()
                );
            }
        }
        return null;
    }

    private static WorldGuardPlugin getWorldGuard() {
        return (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
    }
}