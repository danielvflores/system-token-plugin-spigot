package com.stp.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.java.JavaPlugin;

public class TokenStorage {
    private final JavaPlugin plugin;
    private Connection tokenConnection;
    private Connection playerConnection;

    public TokenStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        setupTokenDatabase();
        setupPlayerDatabase();
    }

    private void setupTokenDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            File dbFile = new File(plugin.getDataFolder(), "tokens.db");
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            tokenConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            Statement stmt = tokenConnection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS tokens (" +
                         "uuid TEXT PRIMARY KEY, " +
                         "tokens INTEGER DEFAULT 0" +
                         ");";
            stmt.executeUpdate(sql);
            stmt.close();

            plugin.getLogger().info("Base de datos de tokens cargada.");
        } catch (Exception e) {
            plugin.getLogger().severe("Error al cargar la base de tokens: " + e.getMessage());
        }
    }

    private void setupPlayerDatabase() {
        try {
            File dbFile = new File(plugin.getDataFolder(), "player.db");
            playerConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            Statement stmt = playerConnection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS player_pickaxes (" +
                         "uuid TEXT PRIMARY KEY, " +
                         "pickaxe TEXT" +
                         ");";
            stmt.executeUpdate(sql);
            stmt.close();

            plugin.getLogger().info("Base de datos de jugadores cargada.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error al cargar la base de jugadores: " + e.getMessage());
        }
    }

    public Connection getTokenConnection() {
        return tokenConnection;
    }

    public Connection getPlayerConnection() {
        return playerConnection;
    }

    public void close() {
        try {
            if (tokenConnection != null) tokenConnection.close();
            if (playerConnection != null) playerConnection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error al cerrar conexiones: " + e.getMessage());
        }
    }
}