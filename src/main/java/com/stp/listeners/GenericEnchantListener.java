// package com.stp.listeners;

// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

// import org.bukkit.Material;
// import org.bukkit.block.Block;
// import org.bukkit.entity.Player;
// import org.bukkit.event.EventHandler;
// import org.bukkit.event.Listener;
// import org.bukkit.event.block.BlockBreakEvent;
// import org.bukkit.inventory.ItemStack;

// import com.stp.core.PrisonEnchantCustom;
// import com.stp.enchants.CustomEnchant;
// import com.stp.enchants.impl.Explosive;
// import com.stp.enchants.impl.Fortune;
// import com.stp.objects.Pickaxe;

// public class GenericEnchantListener implements Listener {

//     private final Set<Material> fortuneBlocks = new HashSet<>();

//     public GenericEnchantListener() {
//         List<String> blockNames = PrisonEnchantCustom.getInstance()
//             .getConfig().getStringList("fortune-blocks");

//         for (String name : blockNames) {
//             Material mat = Material.matchMaterial(name.toUpperCase());
//             if (mat != null) fortuneBlocks.add(mat);
//         }
//     }

//     @EventHandler
//     public void onBlockBreak(BlockBreakEvent event) {
//         Player player = event.getPlayer();
//         ItemStack item = player.getInventory().getItemInHand();

//         if (!Pickaxe.isCustomPickaxe(item)) return;

//         for (String enchantId : PrisonEnchantCustom.getInstance()
//                 .getEnchantmentManager().getRegisteredEnchants()) {

//             int level = Pickaxe.getCustomEnchantmentLevel(item, enchantId);
//             if (level > 0) {
//                 CustomEnchant enchant = PrisonEnchantCustom.getInstance()
//                     .getEnchantmentManager()
//                     .createEnchantment(enchantId, level);

//                 if (enchant != null) {

//                     enchant.applyEffect(player, level);

//                     if (enchant instanceof Explosive) {
//                         ((Explosive) enchant).handleBlockBreak(event, player, level);
//                     }

//                     if (enchant instanceof Fortune) {
//                         Block block = event.getBlock();
//                         Material blockType = block.getType();

//                         if (fortuneBlocks.contains(blockType)) {
//                             event.setCancelled(true);
//                             List<ItemStack> drops = (List<ItemStack>) block.getDrops(item);
//                             block.setType(Material.AIR);

//                             for (ItemStack drop : drops) {
//                                 int newAmount = drop.getAmount() * (1 + level);
//                                 drop.setAmount(newAmount);
//                                 block.getWorld().dropItemNaturally(block.getLocation(), drop);
//                             }
//                         }
//                     }
//                 }
//             }
//         }
//     }
// }
