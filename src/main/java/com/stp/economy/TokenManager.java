package com.stp.economy;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.stp.db.TokenStorage;

public class TokenManager {
    private final TokenStorage db;

    public TokenManager(TokenStorage db) {
        this.db = db;
    }

    public BigDecimal getTokens(UUID uuid) {
        try (PreparedStatement stmt = db.getTokenConnection().prepareStatement(
            "SELECT tokens FROM tokens WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new BigDecimal(rs.getString("tokens"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public void setTokens(UUID uuid, BigDecimal amount) {
        try {
            PreparedStatement checkStmt = db.getTokenConnection().prepareStatement(
                "SELECT 1 FROM tokens WHERE uuid = ?");
            checkStmt.setString(1, uuid.toString());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                PreparedStatement updateStmt = db.getTokenConnection().prepareStatement(
                    "UPDATE tokens SET tokens = ? WHERE uuid = ?");
                updateStmt.setString(1, amount.toPlainString());
                updateStmt.setString(2, uuid.toString());
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = db.getTokenConnection().prepareStatement(
                    "INSERT INTO tokens (uuid, tokens) VALUES (?, ?)");
                insertStmt.setString(1, uuid.toString());
                insertStmt.setString(2, amount.toPlainString());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTokens(UUID uuid, BigDecimal amount) {
        BigDecimal current = getTokens(uuid);
        setTokens(uuid, current.add(amount));
    }

    public boolean removeTokens(UUID uuid, BigDecimal amount) {
        BigDecimal current = getTokens(uuid);
        if (current.compareTo(amount) < 0) {
            return false;
        }
        setTokens(uuid, current.subtract(amount));
        return true;
    }
}
