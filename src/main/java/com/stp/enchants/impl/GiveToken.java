package com.stp.enchants.impl;

import java.math.BigDecimal;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;
import com.stp.utils.MessageUtils;

public class GiveToken implements CustomEnchant {
    private final int level;
    private final String displayName;
    private final int maxLevel;
    private final boolean enabled;
    private final int priceForLevel;
    private final boolean messageStatus;

    public GiveToken(int level) {
        this.level = level;
        this.displayName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("enchants.give-token.display", "Recolector de tokens");
        this.maxLevel = PrisonEnchantCustom.getInstance().getConfig()
                .getInt("enchants.give-token.max-level", 3);
        this.enabled = PrisonEnchantCustom.getInstance().getConfig()
                .getBoolean("enchants.give-token.enabled", true);
        this.priceForLevel = PrisonEnchantCustom.getInstance().getConfig()
                .getInt("enchants.give-token.price-for-level", 10);
        this.messageStatus = PrisonEnchantCustom.getInstance().getConfig()
                .getBoolean("enchants.give-token.messageStatus", true);
    }

    @Override
    public String getId() {
        return "giveToken";
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

    public void handleBlockBreak(Player player, ItemStack item) {
        if (!enabled) return;
        if (item == null || !item.getType().toString().contains("PICKAXE")) return;

        int tokensToGive = level * priceForLevel;
        PrisonEnchantCustom.getInstance().getTokenManager().addTokens(
            player.getUniqueId(),
            BigDecimal.valueOf(tokensToGive)
        );
        if (messageStatus) {
            player.sendMessage(MessageUtils.getMessage(
                "give-token.received",
                "%tokens%", String.valueOf(tokensToGive),
                "%enchant%", getDisplayNamePlain()
            ));
        }
    }

    private String getDisplayNamePlain() {
        String colored = displayName.replace("&", "§");
        return colored.replaceAll("§[0-9a-fk-or]", "");
    }

    @Override
    public void applyEffect(Player player, int level) {
        if (!enabled) return;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return enabled && item != null && (
            item.getType().name().endsWith("_PICKAXE")
        );
    }
}