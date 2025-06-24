package com.stp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class PickaxeStorage {

    private final TokenStorage db;

    public PickaxeStorage(TokenStorage db) {
        this.db = db;
    }

    public void savePickaxe(UUID uuid, ItemStack item) {
        try {
            String encoded = encodeItemStack(item);

            PreparedStatement ps = db.getPlayerConnection().prepareStatement(
                "REPLACE INTO player_pickaxes (uuid, pickaxe) VALUES (?, ?);"
            );
            ps.setString(1, uuid.toString());
            ps.setString(2, encoded);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ItemStack loadPickaxe(UUID uuid) {
        try {
            PreparedStatement ps = db.getPlayerConnection().prepareStatement(
                "SELECT pickaxe FROM player_pickaxes WHERE uuid = ?;"
            );
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String base64 = rs.getString("pickaxe");
                rs.close();
                ps.close();
                return decodeItemStack(base64);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deletePickaxe(UUID uuid) {
        try {
            PreparedStatement ps = db.getPlayerConnection().prepareStatement(
                "DELETE FROM player_pickaxes WHERE uuid = ?;"
            );
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String encodeItemStack(ItemStack item) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", item);
        return config.saveToString();
    }

    private ItemStack decodeItemStack(String data) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config.getItemStack("i");
    }
}
