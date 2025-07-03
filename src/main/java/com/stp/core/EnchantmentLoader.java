package com.stp.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.logging.Level;

import com.stp.enchants.CustomEnchant;

public class EnchantmentLoader {
    private final SystemTokenEnchant plugin;
    private final File enchantsFolder;

    public EnchantmentLoader(SystemTokenEnchant plugin) {
        this.plugin = plugin;
        this.enchantsFolder = new File(plugin.getDataFolder(), "enchants");
        if (!enchantsFolder.exists()) enchantsFolder.mkdirs();
    }

    public void loadEnchantments() {
        File[] jarFiles = enchantsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null) return;

        for (File jarFile : jarFiles) {
            try (JarFile jar = new JarFile(jarFile)) {
                URLClassLoader loader = new URLClassLoader(
                    new URL[]{jarFile.toURI().toURL()},
                    plugin.getClass().getClassLoader()
                );

                jar.stream().forEach(entry -> {
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                        try {
                            Class<?> clazz = loader.loadClass(className);
                            if (CustomEnchant.class.isAssignableFrom(clazz)) {
                                Class<? extends CustomEnchant> enchantClass = 
                                    (Class<? extends CustomEnchant>) clazz;
                                CustomEnchant enchant = enchantClass
                                    .getConstructor(int.class)
                                    .newInstance(1);
                                plugin.getEnchantmentManager()
                                    .registerEnchantment(enchant.getId(), enchantClass);
                                plugin.getLogger().info("Encantamiento cargado: " + enchant.getId());
                            }
                        } catch (Exception e) {
                            plugin.getLogger().log(Level.WARNING, "Error al cargar clase: " + className, e);
                        }
                    }
                });
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error al cargar JAR: " + jarFile.getName(), e);
            }
        }
    }
}