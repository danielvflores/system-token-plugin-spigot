package com.stp.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.stp.commands.EnchantReloadCommand;
import com.stp.commands.TokenCommand;
import com.stp.commands.object.GivePickaxeCommand;
import com.stp.commands.utility.TokenTabCompleter;
import com.stp.db.DatabaseManager;
import com.stp.economy.TokenManager;
import com.stp.enchants.CustomEnchant;
import com.stp.enchants.impl.Efficiency;
import com.stp.enchants.impl.Explosive;
import com.stp.enchants.impl.Fly;
import com.stp.enchants.impl.Fortune;
import com.stp.enchants.impl.GiveToken;
import com.stp.enchants.impl.Nuke;
import com.stp.enchants.impl.Speed;
import com.stp.listeners.EnchantEffectTask;
import com.stp.listeners.PickaxeListener;

public class PrisonEnchantCustom extends JavaPlugin {
    private static PrisonEnchantCustom instance;
    private DatabaseManager databaseManager;
    private TokenManager tokenManager;
    private EnchantmentManager enchantmentManager;

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.databaseManager = new DatabaseManager(this);
        this.tokenManager = new TokenManager(databaseManager);
        this.enchantmentManager = new EnchantmentManager();
        
        Class<? extends CustomEnchant>[] enchantClasses;
        enchantClasses = new Class[] {
            Speed.class,
            Explosive.class,
            Efficiency.class,
            Fortune.class,
            Fly.class,
            Nuke.class,
            GiveToken.class 
        };

        for (Class<? extends CustomEnchant> enchantClass : enchantClasses) {
            try {
                CustomEnchant enchant = enchantClass.getConstructor(int.class).newInstance(0);

                getEnchantmentManager().registerEnchantment(enchant.getId(), enchantClass);

                getLogger().info("Encantamiento registrado: " + enchant.getId());
            } catch (Exception e) {
                getLogger().warning("No se pudo registrar encantamiento: " + enchantClass.getSimpleName());
                e.printStackTrace();
            }
        }

        new EnchantmentLoader(this).loadEnchantments();
        new EnchantEffectTask().runTaskTimer(this, 0, 20);

        TokenCommand tokenCommand = new TokenCommand(tokenManager);
        getCommand("token").setExecutor(tokenCommand);
        getCommand("token").setTabCompleter(new TokenTabCompleter());
        getCommand("givepickaxe").setExecutor(new GivePickaxeCommand());
        getCommand("enchantsreload").setExecutor(new EnchantReloadCommand());
        getServer().getPluginManager().registerEvents(new PickaxeListener(), this);


        getLogger().info("Plugin habilitado correctamente.");
    }


    @Override
    public void onDisable() {
        if (databaseManager != null) databaseManager.close();
    }

    public static PrisonEnchantCustom getInstance() { return instance; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public TokenManager getTokenManager() { return tokenManager; }
    public EnchantmentManager getEnchantmentManager() { return enchantmentManager; }

    public List<Material> getAllowedBlocks() {
        List<String> blockNames = getConfig().getStringList("allowed-blocks");
        List<Material> allowedBlocks = new ArrayList<>();
        for (String name : blockNames) {
            try {
                allowedBlocks.add(Material.valueOf(name));
            } catch (IllegalArgumentException e) {
                getLogger().warning("Bloque inválido en configuración: " + name);
            }
        }
        return allowedBlocks;
    }
}